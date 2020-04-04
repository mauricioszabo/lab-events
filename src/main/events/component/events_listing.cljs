(ns events.component.events-listing
  (:require [events.component.event-item :refer [event-item]]
            [events.component.pagination :refer [pagination]]))

(defn events-listing [{:keys [events on-favorite-change] :as attrs}]
  (when (seq events)
    [:<>
     [:div.row
      [:div.col.l12
       [:ul.collection {:data-testid "events-listing"}
        (for [event events]
          [event-item
           {:key (:id event)
            :event event
            :on-favorite-change on-favorite-change}])]]]

     [:div.row
      [:div.col.l12.center-align
       [pagination (select-keys attrs [:page :on-goto-page])]]]]))
