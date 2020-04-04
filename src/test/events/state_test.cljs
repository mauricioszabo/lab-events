(ns events.state-test
  (:require [events.state :as state]
            [events.test-helpers :as helpers]
            [cljs.test :refer [deftest is testing use-fixtures]]))

(use-fixtures :each {:before state/reset-state!})

(deftest set-search-param!-tests
  (testing "set-search-param! sets the parameter value"
    (is (= "" (:city (state/search-params))))

    (state/set-search-param! :city "Berlin")
    (is (= "Berlin" (:city (state/search-params))))))

(deftest set-search-result!-tests
  (testing "set-search-result! sets fav? as false for all events "
    (let [events (helpers/create-events 3)]
      (state/set-search-result! events {})
      (is (every? false? (map :fav? (state/events))))))

  (testing "set-search-result! sets the events page as the result page"
    (let [page {:number 2, :total-pages 10}]
      (state/set-search-result! {} page)
      (is (= page (state/events-page))))))

(deftest update-event-fav-status!-tests
  (testing "update fav state switches the current true/false state to false/true"
    (let [event (helpers/create-event)]
      (state/set-search-result! [event] {})
      ;; sanity check, set-search-result! sets fav status to false
      (is (not (-> (state/events) first :fav?)))

      ;; false -> true
      (state/update-event-fav-status! (:id event))
      (is (-> (state/events) first :fav?))

      ;; true -> false
      (state/update-event-fav-status! (:id event))
      (is (not (-> (state/events) first :fav?))))))
