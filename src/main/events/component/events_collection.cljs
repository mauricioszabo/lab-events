(ns events.component.events-collection
  (:require [events.component.event-item :refer [event-item]]))

(defn events-collection [{:keys [events on-fav-selected]}]
  (when (seq events)
    [:ul.collection
     (for [event events]
       [event-item {:key (:id event)
                    :event event
                    :on-fav-selected on-fav-selected}])]))
