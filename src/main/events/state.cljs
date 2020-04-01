(ns events.state
  (:require [reagent.core :as reagent]))

(def app-state
  (reagent/atom {:search-params {}
                 :events {}
                 :favorites {}
                 :page {}
                 :display :all}))

(defn search-params []
  (:search-params @app-state))

(defn search-params-cursor []
  (reagent/cursor app-state [:search-params]))

(defn events []
  (:events @app-state))

(defn favorites []
  (:favorites @app-state))

(defn page []
  (:page @app-state))

(defn current-page-number []
  (:number (page)))

(defn display-mode []
  (:display @app-state))

(defn set-display-mode! [mode]
  (if (= mode :favorites)
    (swap! app-state
           (fn [state]
             (assoc state
                    :events (:favorites state)
                    :display :favorites)))
    (swap! app-state identity)))

(defn set-search-result!
  ([events page]
   (set-search-result! events page false))
  ([events page pagination]
   (swap! app-state
          (fn [state]
            (let [fav? (if pagination
                         #(contains? (:favorites state) (:id %))
                         (constantly false))
                  events (->> events
                              (map #(assoc %1 :favorite? (fav? %1)))
                              (reduce #(assoc %1 (:id %2) %2) {}))]
              (assoc state
                     :events events
                     :page page
                     :favorites (if pagination (:favorites state) {})
                     :display :all))))))

(defn set-goto-page-result! [events page]
  (set-search-result! events page true))

(defn set-favorite! [event-id]
  (swap! app-state
         (fn [state]
           (let [state (update-in state [:events event-id :favorite?] not)]
             (if (contains? (:favorites state) event-id)
               (update state :favorites dissoc event-id)
               (update state :favorites assoc event-id (get-in state [:events event-id])))))))
