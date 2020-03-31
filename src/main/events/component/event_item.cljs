(ns events.component.event-item
  (:require [clojure.string :as str]
            [reagent.format]))

(defn select-image [images]
  (first images))

(defn event-image [{:keys [images]}]
  (let [image (select-image images)]
    [:img.circle {:src (:url image)}]))

(defn event-title [{:keys [name url]}]
  [:span.title
   [:a {:href url, :target "_blank"} name]])

(defn event-date [{{start-date :start} :dates}]
  (let [[year month day] (str/split (:local-date start-date) #"-")
        [hour minute] (str/split (:local-time start-date) #":")]
    [:p.valign-wrapper
     [:i.material-icons.event-detail-icon "schedule"]
     (reagent.format/format "%s.%s.%s %s:%s" day month year hour minute)]))

(defn event-price [price tooltip]
  [:span.tooltipped.event-price
   {:data-position "top"
    :data-tooltip tooltip
    :ref #(-> js/M .-Tooltip (.init %))}
   (reagent.format/format "%.2f" price)])

(defn event-prices [{:keys [id price-ranges]}]
  [:p.valign-wrapper
   [:i.material-icons.event-detail-icon "payment"]
   (for [[idx {price :min, type :type}] (zipmap (range (count price-ranges))
                                                price-ranges)]
     ^{:key (str id "__event-item__price__" idx)} [event-price price type])])

(defn event-fav-icon [{:keys [id favorite?]} on-fav-selected]
  [:a.secondary-content
   {:href "#!"
    :class (if favorite? "event-fav-on" "event-fav-off")
    :on-click (fn [e]
                (.preventDefault e)
                (on-fav-selected id))}
   [:i.material-icons "star"]])

(defn event-item [{:keys [event on-fav-selected]}]
  [:li.collection-item.avatar
   [event-image event]
   [event-title event]
   [event-date event]
   [event-prices event]
   [event-fav-icon event on-fav-selected]])
