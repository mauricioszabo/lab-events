(ns events.state
  (:require [reagent.core :as reagent]))

(defn initial-state []
  {:search-params {:city ""
                   :sort "name,asc"}
   :events {:items {}
            :page {}}})

(def state (reagent/atom (initial-state)))

(defn reset-state! []
  (reset! state (initial-state)))

(defn search-params []
  (:search-params @state))

(defn set-search-param! [name value]
  (swap! state update :search-params assoc name value))

(defn set-search-result! [events page]
  (let [events (->> events
                    (map #(assoc % :fav? false))
                    (reduce #(assoc %1 (:id %2) %2) {}))]
    (swap! state update :events assoc
           :items events
           :page page)))

(defn events []
  (-> @state (get-in [:events :items]) vals))

(defn events-page []
  (get-in @state [:events :page]))

(defn update-event-fav-status! [id]
  (swap! state update-in [:events :items id :fav?] not))
