(ns events.core
  (:require [events.component.search-form :refer [search-form]]
            [reagent.core :as reagent]
            [reagent.dom]))

(defn search-events [country city]
  (.log js/console (str country ", " city)))

(defn app []
  [:div
   [:div.row [search-form search-events]]])

(defn ^:export main []
  (reagent.dom/render
   [app]
   (.getElementById js/document "app")))
