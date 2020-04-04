(ns events.component.events-listing-test
  (:require [events.component.events-listing :refer [events-listing]]
            [events.test-helpers :as helpers]
            [cljs.test :refer [deftest is testing use-fixtures]]
            ["@testing-library/react" :as react]))

(use-fixtures :each {:after react/cleanup})

(deftest smoke
  (testing "listing isn't displayed if there are no events to show"
    (helpers/with-mounted-component
      [events-listing
       {:events []
        :on-favorite-change identity}]
      #(helpers/element-doesnt-exist-by-testid % "events-listing")))

  (testing "listing is displayed if there is at least one event to show"
    (helpers/with-mounted-component
      [events-listing
       {:events [(helpers/create-event)]
        :on-favorite-change identity}]
      #(is (helpers/element-exists-by-testid % "events-listing"))))

  (testing "listing displays all events (minus pagination concerns)"
    (let [events (helpers/create-events 3)]
      (helpers/with-mounted-component
        [events-listing
         {:events events
          :on-favorite-change identity}]
        #(doseq [{:keys [name]} events]
           (is (helpers/element-exists-by-text % name)))))))
