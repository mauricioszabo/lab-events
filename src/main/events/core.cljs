(ns events.core
  (:require [reagent.core :as reagent]
            [reagent.dom]
            [events.component.search-form :refer [search-form]]
            [events.component.events-collection :refer [events-collection]]
            [events.component.pagination :refer [pagination]]
            [events.ticketmaster :as ticketmaster]))

(def app-state (reagent/atom {}))

(defn handle-search-success [events page]
  (let [events (->> events
                    (map #(assoc % :favorite? false))
                    (reduce #(assoc %1 (:id %2) %2) {}))]
    (swap! app-state assoc :events events :page page :favorites {})))

(defn handle-search-error [error]
  (.log js/console error))

(defn search-events [params]
  (ticketmaster/find-events params
                            handle-search-success
                            handle-search-error))

(defn update-favorite-status [id]
  (swap! app-state
         (fn [state]
           (let [state (update-in state [:events id :favorite?] not)]
             (if (contains? (:favorites state) id)
               (update state :favorites dissoc id)
               (update state :favorites assoc id (get-in state [:events id])))))))

(defn handle-goto-page-success [events page]
  (let [favorites (:favorites @app-state)
        events (->> events
                    (map #(assoc %1 :favorite? (contains? favorites (:id %1))))
                    (reduce #(assoc %1 (:id %2) %2) {}))]
    (swap! app-state assoc :events events :page page)))

(defn goto-page [n]
  (ticketmaster/find-events
   (assoc (:search @app-state) :page n)
   handle-goto-page-success
   handle-search-error))

(defn app []
  [:<>
   [:div.row
    [search-form
     {:state (reagent/cursor app-state [:search])
      :on-search search-events}]]

   [:div.row
    [:div.col.l10
     [events-collection
      {:events (->> @app-state :events vals (sort-by :name))
       :on-fav-selected update-favorite-status}]]]

   [:div.row
    [:div.col.l10
     [pagination
      {:page (:page @app-state)
       :on-goto-page goto-page}]]]])

(defn ^:export main []
  (reagent.dom/render
   [app]
   (.getElementById js/document "app")))
