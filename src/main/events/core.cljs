(ns events.core
  (:require [reagent.dom]
            [events.state :as state]
            [events.component.search-form :refer [search-form]]
            [events.component.events-listing :refer [events-listing]]
            [events.ticketmaster :as ticketmaster]))

(defn handle-search-error [error]
  (.log js/console error))

(defn search-events []
  (ticketmaster/find-events (state/search-params)
                            state/set-search-result!
                            handle-search-error))

(defn paginate [n]
  (ticketmaster/find-events (assoc (state/search-params) :page n)
                            state/set-search-result!
                            handle-search-error))

(defn app []
  [:<>
   [search-form
    {:field-values (state/search-params)
     :on-field-value-change state/set-search-param!
     :on-search search-events}]

   [events-listing
    {:events (state/events)
     :page (state/events-page)
     :on-favorite-change state/update-event-fav-status!
     :on-goto-page paginate}]])

(defn ^:export main []
  (reagent.dom/render
   [app]
   (.getElementById js/document "app")))
