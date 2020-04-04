(ns events.component.search-form)

(defn search-form [{:keys [field-values on-field-value-change on-search]}]
  [:div.row
   [:form.col.l12
    [:div.row
     [:div.input-field.col.l5
      [:label {:for "search-form__country"} "Country"]
      [:input
       {:id "search-form__country"
        :type "text"
        :disabled true}]]

     [:div.input-field.col.l5
      [:label {:for "search-form__city"} "City"]
      [:input
       {:id "search-form__city"
        :type "text"
        :value (:city field-values)
        :on-change #(on-field-value-change :city (-> % .-target .-value))}]]

     [:div.input-field.col.l2.valign-wrapper
      [:button.btn.waves-effect.waves-light
       {:type "submit"
        :on-click (fn [e]
                    (.preventDefault e)
                    (on-search))
        :data-testid "search-form__search-btn"}
       [:i.material-icons "search"]]]]]])
