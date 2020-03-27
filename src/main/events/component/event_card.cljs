(ns events.component.event-card)

(defn select-image [images]
  ;; TODO change to something else
  (first images))

(defn event-card-image [{:keys [images]}]
  (when-let [image (select-image images)]
    [:div.card-image
     [:img {:src (:url image)
            :data-testid "event-card-image__img"}]]))

(defn event-card-fav-icon [{:keys [id favorite?]} on-favorite-fn]
  (let [color (if favorite? "yellow accent-4" "grey lighten-1")
        btn-class (str "btn-floating btn-small waves-effect waves-light right " color)]
    [:a
     {:class btn-class
      :on-click (fn [e]
                  (.preventDefault e)
                  (on-favorite-fn id))}
     [:i.material-icons "star"]]))

(defn event-card-content [{:keys [name description url dates] :as event} on-favorite-fn]
  [:div.card-content
   [:span.card-title
    [:a {:href url} name]
    [event-card-fav-icon event on-favorite-fn]]])

(defn event-card [{:keys [id] :as event} on-favorite-fn]
  [:div.col.l6
   [:div.card ^{:key id}
    [event-card-image event]
    [event-card-content event on-favorite-fn]]])
