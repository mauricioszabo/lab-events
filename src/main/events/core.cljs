(ns events.core
  (:require [reagent.core :as reagent]
            [reagent.dom]))

(defn app []
  (let [counter (reagent/atom 0)]
    (fn [txt]
      [:button.green
       {:on-click #(swap! counter inc)}
       (str txt " - " @counter)])))

(defn ^:export main []
  (reagent.dom/render [app "Hello, Reagent"] (.getElementById js/document "app")))
