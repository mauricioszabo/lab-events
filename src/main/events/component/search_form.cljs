(ns events.component.search-form)

(defn set-field-value! [state field element]
  (swap! state assoc field (-> element .-target .-value)))

(defn search-form [{:keys [state on-search]}]
  [:form.col.l12
   [:div.row
    [:div.input-field.col.l5
     [:label {:for "search-form__country"} "Country"]
     [:input#search-form__country
      {:type "text"
       :value (:country @state)
       :on-change (partial set-field-value! state :country)}]]

    [:div.input-field.col.l5
     [:label {:for "search-form__city"} "City"]
     [:input#search-form__city
      {:type "text"
       :value (:city @state)
       :on-change (partial set-field-value! state :city)}]]

    [:div.input-field.col.l2
     [:button.btn.waves-effect.waves-light
      {:type "submit"
       :on-click (fn [e]
                   (.preventDefault e)
                   (on-search @state))}
      [:i.material-icons "search"]]]]])
