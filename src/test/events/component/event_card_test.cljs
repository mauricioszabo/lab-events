(ns events.component.event-card-test
  (:require [cljs.test :refer [deftest is testing use-fixtures]]
            [events.component.event-card :refer [event-card]]
            [reagent.core :as reagent]
            ["@testing-library/react" :as react]))

(use-fixtures :each {:after react/cleanup})

(defn create-event
  [& {:keys [id name description url images dates favorite?]
      :or {id "id"
           name "name"
           description "description"
           url "https://example.com"
           images [{:url "https://example.com/image.png"
                    :width 1024
                    :height 768
                    :ratio "4_3"}]
           dates []
           favorite? false}}]
  {:id id
   :name name
   :description description
   :url url
   :images images
   :dates dates
   :favorite? favorite?})

(defn mount-component [component]
  (-> component reagent/as-element react/render))

(deftest event-card-image-tests
  (testing "image should be displayed if event has at least one image"
    (let [event (create-event)
          component (mount-component [event-card event identity])
          node (.getByTestId component "event-card-image__img")]
      (is (= (-> event :images first :url)
             (.-src node))))))
