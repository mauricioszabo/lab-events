(ns events.component.events-collection
  (:require [events.component.event-item :refer [event-item]]
            [events.component.pagination :refer [pagination]]))

(defn display-mode-toggle [{:keys [display-mode on-display-all on-display-favorites]}]
  (if (= display-mode :all)
    [:a
     {:href "#!"
      :on-click on-display-favorites}
     "Favorites Only"]
    [:a
     {:href "#!"
      :on-click on-display-all}
     "All"]))

(defn events-collection [{:keys [events on-fav-selected display-mode] :as params}]
  (when (seq events)
    [:div.row
     [:div.row
      [:div.col.l2
       [display-mode-toggle params]]]

     [:div.row
      [:div.col.l12
       [:ul.collection
        (for [event events]
          [event-item {:key (:id event)
                       :event event
                       :on-fav-selected on-fav-selected}])]]]

     (when (= display-mode :all)
       [:div.row
        [:div.col.l12
         [pagination params]]])]))
