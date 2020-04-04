(ns events.component.pagination)

(defn pagination [{:keys [page on-goto-page]}]
  (let [{total-pages :total-pages, current :number} page
        total-pages (min 10 total-pages)]
    (when (> total-pages 1)
      [:ul.pagination {:role "pagination"}
       [:li
        {:class (when (zero? current) "disabled")
         :role "pagination__previous"}
        [:a
         {:href "#!"
          :on-click #(on-goto-page (dec current))
          :data-testid "pagination__previous-a"}
         [:i.material-icons "chevron_left"]]]

       (for [i (range total-pages)]
         [:li
          {:key (str "pagination__" i)
           :class (if (= i current) "active" "waves-effect")}
          [:a
           {:href "#!"
            :on-click #(on-goto-page i)
            :role "pagination__page"}
           (inc i)]])

       [:li
        {:class (when (= current (dec total-pages)) "disabled")
         :role "pagination__next"}
        [:a
         {:href "#!"
          :on-click #(on-goto-page (inc current))
          :data-testid "pagination__next-a"}
         [:i.material-icons "chevron_right"]]]])))
