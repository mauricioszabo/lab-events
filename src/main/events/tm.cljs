(ns events.tm
  (:require [ajax.core :as ajax]
            [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :as csk.extras]))

(defonce endpoint-url "https://app.ticketmaster.com/discovery/v2/events.json")

(defonce api-key "4diZ7tJQArO9sxHiVWMASl5udVTAoI78")

(defn extract-events [response]
  (->> (get-in response ["_embedded" "events"])
       (map #(select-keys % ["id" "name" "description" "url" "images" "dates"]))
       (map (partial csk.extras/transform-keys csk/->kebab-case-keyword))))

(defn extract-page [response]
  (->> (get response "page")
       (csk.extras/transform-keys csk/->kebab-case-keyword)))

(defn fetch-events
  [success-fn error-fn params]
  (ajax/GET endpoint-url
            {:handler #(success-fn {:events (extract-events %)
                                    :page (extract-page %)})
             :error-handler error-fn
             :url-params (merge {:apikey api-key} params)}))
