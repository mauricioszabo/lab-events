(ns events.component.search-form
  (:require [reagent.core :as reagent]))

(defn search-form [on-search-fn]
  (let [state (reagent/atom {})]
    (fn [on-search-fn]
      [:form.col.l12
       [:div.row
        ;; country
        [:div.input-field.col.l5
         [:label {:for "search-form__country"} "Country"]
         [:input#search-form__country
          {:type "text"
           :on-change #(swap! state assoc :country (-> % .-target .-value))}]]

        ;; city
        [:div.input-field.col.l5
         [:label {:for "search-form__city"} "City"]
         [:input#search-form__city
          {:type "text"
           :on-change #(swap! state assoc :city (-> % .-target .-value))}]]

        ;; search button
        [:div.input-field.col.l2
         [:button.btn.waves-effect.waves-light
          {:type "submit"
           :on-click (fn [e]
                       (.preventDefault e)
                       (on-search-fn (:country @state)
                                     (:city @state)))}
          [:i.material-icons "search"]]]]])))
