(ns events.component.event-item
  (:require [clojure.string :as str]
            [reagent.format]))

(defn select-image [images]
  (first images))

(defn event-image [{:keys [images]}]
  (when-let [image (select-image images)]
    [:img.circle
     {:src (:url image)
      :data-testid "event-item__img"}]))

(defn event-title [{:keys [name url]}]
  [:span.title
   [:a {:href url, :target "_blank"} name]])

(defn event-date [{{:keys [start]} :dates}]
  [:p.valign-wrapper
   [:i.material-icons.event-detail-icon "schedule"]
   (if start
     (let [[year month day] (str/split (:local-date start) #"-")
           [hour minute] (str/split (:local-time start) #":")]
       (reagent.format/format "%s.%s.%s %s:%s" day month year hour minute))
     "TBD")])

(defn event-price [price tooltip]
  [:span.tooltipped.event-price
   {:data-position "top"
    :data-tooltip tooltip
    :ref #(-> js/M .-Tooltip (.init %))}
   (reagent.format/format "%.2f" price)])

(defn event-prices [{:keys [id price-ranges]}]
  [:p.valign-wrapper
   [:i.material-icons.event-detail-icon "payment"]
   (let [prices (for [[idx {price :min, type :type}]
                      (zipmap (range (count price-ranges))
                              price-ranges)]
                  ^{:key (str id "__event-item__price__" idx)}
                  [event-price price type])]
     (if (seq prices)
       prices
       "PRICES NOT AVAILABLE"))])

(defn event-favorite-icon [{:keys [id fav?]} on-favorite-change]
  [:a.secondary-content
   {:href "#!"
    :class (if fav? "event-favorite-on" "event-favorite-off")
    :on-click #(on-favorite-change id)
    :data-testid "event-item__favorite-icon"}
   [:i.material-icons "star"]])

(defn event-item [{:keys [event on-favorite-change]}]
  [:li.collection-item.avatar
   [event-image event]
   [event-title event]
   [event-date event]
   [event-prices event]
   [event-favorite-icon event on-favorite-change]])
