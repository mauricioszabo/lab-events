(ns events.test-helpers
  (:require [clojure.string :as str]
            [reagent.core :as reagent]
            [reagent.format]
            ["@testing-library/react" :as react]))

(defn with-mounted-component [component f]
  (let [mounted (-> component reagent/as-element react/render)]
    (try
      (f mounted)
      (finally
        (.unmount mounted)
        (reagent/flush)))))

(defn element-doesnt-exist-by-text [component text]
  (nil? (.queryByText component text)))

(def element-exists-by-text
  (comp not element-doesnt-exist-by-text))

(defn element-doesnt-exist-by-testid [component id]
  (nil? (.queryByTestId component id)))

(def element-exists-by-testid
  (comp not element-doesnt-exist-by-testid))

(defn element-doesnt-exist-by-role [component role]
  (nil? (.queryByRole component role)))

(def element-exists-by-role
  (comp not element-doesnt-exist-by-role))

(defn create-event
  [& {:keys [id name url images dates price-ranges fav?]
      :or {id "event-id"
           name "event-name"
           url "https://example.com"
           images [{:url "https://example.com/image.jpg"
                    :ratio "4_3"}]
           dates {:start {:local-date "2020-10-10"
                          :local-time "12:00:00"}}
           price-ranges [{:type "regular"
                          :min 150.0}]
           fav? false}}]
  {:id id
   :name name
   :url url
   :images images
   :dates dates
   :price-ranges price-ranges
   :fav? fav?})

(defn create-events [n & {:as properties}]
  (for [id (range (inc n))]
    (let [ps (assoc properties
                    :id (str id)
                    :name (str "event-" id))]
      (apply create-event (flatten (seq ps))))))

(defn format-event-date [{{{local-date :local-date} :start} :dates}]
  (let [[year month day] (str/split local-date #"-")]
    (str day "." month "." year)))

(defn format-event-time [{{{local-time :local-time} :start} :dates}]
  (let [[hour minute] (str/split local-time #":")]
    (str hour ":" minute)))

(defn format-event-price [price]
  (reagent.format/format "%.2f" price))

(defn format-event-prices [{:keys [price-ranges]}]
  (->> price-ranges (map :min) (map format-event-price)))
