(ns events.core
  (:require [reagent.dom]
            [events.component.search-form :refer [search-form]]
            [events.component.events-collection :refer [events-collection]]
            [events.state :as state]
            [events.ticketmaster :as ticketmaster]))

(defn handle-search-error [error]
  (.log js/console error))

(defn search-events [params]
  (ticketmaster/find-events params
                            state/set-search-result!
                            handle-search-error))

(defn goto-page [n]
  (ticketmaster/find-events (assoc (state/search-params) :page n)
                            state/set-goto-page-result!
                            handle-search-error))

(defn app []
  [:<>
   [:div.row
    [search-form
     {:state (state/search-params-cursor)
      :on-search search-events}]]

   [:div.row
    [:div.col.l10
     [events-collection
      {:events (->> (state/events) vals (sort-by :name))
       :on-fav-selected state/set-favorite!
       :display-mode (state/display-mode)
       :on-display-all #(goto-page (state/current-page-number))
       :on-display-favorites #(state/set-display-mode! :favorites)
       :page (state/page)
       :on-goto-page goto-page}]]]])

(defn ^:export main []
  (reagent.dom/render
   [app]
   (.getElementById js/document "app")))
