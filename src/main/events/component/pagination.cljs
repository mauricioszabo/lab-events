(ns events.component.pagination)

(defn pagination [{:keys [page on-goto-page]}]
  (let [{total-pages :total-pages, current :number} page
        total-pages (min 10 total-pages)]
    (when (> total-pages 1)
      [:ul.pagination.center-align
       [:li
        {:class (when (zero? current) "disabled")}
        [:a
         {:href "#!"
          :on-click #(on-goto-page (dec current))}
         [:i.material-icons "chevron_left"]]]

       (for [idx (range total-pages)]
         [:li
          {:key (str "pagination__" idx)
           :class (if (= idx current) "active" "waves-effect")}
          [:a
           {:href "#!"
            :on-click #(on-goto-page idx)}
           (inc idx)]])

       [:li
        {:class (when (= current (dec total-pages)) "disabled")}
        [:a
         {:href "#!"
          :on-click #(on-goto-page (inc current))}
         [:i.material-icons "chevron_right"]]]])))
