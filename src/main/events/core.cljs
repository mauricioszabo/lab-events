(ns events.core
  (:require [reagent.dom]
            [events.state :as state]
            [events.component.search-form :refer [search-form]]
            [events.component.events-listing :refer [events-listing]]
            [events.ticketmaster :as ticketmaster]))

(defn handle-search-success [events page]
  (.log js/console events page))

(defn handle-search-error [error]
  (.log js/console error))

(defn search-events []
  (ticketmaster/find-events (state/search-params)
                            handle-search-success
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
     :on-goto-page identity}]])

(defn ^:export main []
  (reagent.dom/render
   [app]
   (.getElementById js/document "app")))
