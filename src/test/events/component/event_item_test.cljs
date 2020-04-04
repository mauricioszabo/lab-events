(ns events.component.event-item-test
  (:require [events.component.event-item :refer [event-item]]
            [events.test-helpers :as helpers]
            [clojure.string :as str]
            [cljs.test :refer [deftest is testing use-fixtures]]
            ["@testing-library/react" :as react]))

(use-fixtures :each {:after react/cleanup})

(deftest event-image-tests
  (testing "item doesn't display image if the event doesn't have images"
    (helpers/with-mounted-component
      [event-item
       {:event (helpers/create-event :images [])
        :on-favorite-change identity}]
      #(is (helpers/element-doesnt-exist-by-testid % "event-item__img"))))

  (testing "item displays an image if the event has at least one image"
    (helpers/with-mounted-component
      [event-item
       {:event (helpers/create-event)
        :on-favorite-change identity}]
      #(is (helpers/element-exists-by-testid % "event-item__img")))))

(deftest event-title-tests
  (testing "item displays the event title"
    (let [event (helpers/create-event)]
      (helpers/with-mounted-component
        [event-item
         {:event event
          :on-favorite-change identity}]
        #(is (helpers/element-exists-by-text % (:name event)))))))

(deftest event-date-tests
  (testing "item displays TBD if the event doesn't have dates"
    (helpers/with-mounted-component
      [event-item
       {:event (helpers/create-event :dates [])
        :on-favorite-change identity}]
      #(is (helpers/element-exists-by-text % "TBD"))))

  (testing "item displays the event date if the event has a start date"
    (let [event (helpers/create-event)
          date-text (str (helpers/format-event-date event) " "
                         (helpers/format-event-time event))]
      (helpers/with-mounted-component
        [event-item
         {:event event
          :on-favorite-change identity}]
        #(do
           (is (helpers/element-exists-by-text %1 date-text))
           (is (helpers/element-doesnt-exist-by-text %1 "TBD")))))))

(deftest event-prices-tests
  (testing "item displays PRICES NOT AVAILABLE if the event doesn't have prices"
    (helpers/with-mounted-component
      [event-item
       {:event (helpers/create-event :price-ranges [])
        :on-favorite-change identity}]
      #(is (helpers/element-exists-by-text % "PRICES NOT AVAILABLE"))))

  (testing "item displays the price if the event has a single price"
    (let [event (helpers/create-event)
          price-text (helpers/format-event-price (-> event :price-ranges first :min))]
      (helpers/with-mounted-component
        [event-item
         {:event event
          :on-favorite-change identity}]
        #(is (helpers/element-exists-by-text % price-text)))))

  (testing "item displays all the event prices"
    (let [event (helpers/create-event :price-ranges [{:min 100.0, :type "regular"}
                                                     {:min 230.0, :type "vip"}])
          price-texts (helpers/format-event-prices event)]
      (helpers/with-mounted-component
        [event-item
         {:event event
          :on-favorite-change identity}]
        #(doseq [text price-texts]
           (is (helpers/element-exists-by-text % text)))))))

(deftest event-favorite-icon-tests
  (testing "item displays 'OFF' favorite icon if the event isn't favorite"
    (helpers/with-mounted-component
      [event-item
       {:event (helpers/create-event :fav? false)
        :on-favorite-change identity}]
      #(let [el (.getByTestId % "event-item__favorite-icon")]
         (is (str/includes? (.-className el) "event-favorite-off")))))

  (testing "items displays 'ON' favorite icon if the event is favorite"
    (helpers/with-mounted-component
      [event-item
       {:event (helpers/create-event :fav? true)
        :on-favorite-change identity}]
      #(let [el (.getByTestId % "event-item__favorite-icon")]
         (is (str/includes? (.-className el) "event-favorite-on")))))

  (testing "on-favorite-change is fired with the event id when the favorite icon is clicked"
    (let [event (helpers/create-event)
          store (atom nil)]
      (helpers/with-mounted-component
        [event-item
         {:event event
          :on-favorite-change #(reset! store %)}]
        #(let [el (.getByTestId % "event-item__favorite-icon")]
           (.click react/fireEvent el)
           (is (= (:id event) @store)))))))
