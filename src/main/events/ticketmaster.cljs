(ns events.ticketmaster
  (:require [ajax.core :as ajax]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as csk.extras]))

(def endpoint-url "https://app.ticketmaster.com/discovery/v2/events.json")

(def api-key "4diZ7tJQArO9sxHiVWMASl5udVTAoI78")

(defn extract-events [response]
  (->> (get-in response ["_embedded" "events"])
       (map #(select-keys % ["id" "name" "description" "url" "images" "dates" "priceRanges"]))
       (map #(csk.extras/transform-keys csk/->kebab-case-keyword %))))

(defn extract-page [response]
  (->> (get response "page") (csk.extras/transform-keys csk/->kebab-case-keyword)))

(defn find-events [params on-success on-error]
  (ajax/GET endpoint-url
            {:handler (fn [response]
                        (on-success (extract-events response)
                                    (extract-page response)))
             :error-handler on-error
             :response-format :json
             :url-params (merge {:size 10, :page 0, :sort "name,asc"} params {:apikey api-key})}))
