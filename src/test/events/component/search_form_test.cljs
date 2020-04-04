(ns events.component.search-form-test
  (:require [events.component.search-form :refer [search-form]]
            [events.test-helpers :as helpers]
            [cljs.test :refer [deftest is testing use-fixtures]]
            ["@testing-library/react" :as react]))

(use-fixtures :each {:after react/cleanup})

(deftest smoke
  (testing "all fields are rendered"
    (helpers/with-mounted-component
      [search-form
       {:field-values {:country-code ""
                       :city ""}
        :on-field-value-change identity
        :on-search identity}]
      (fn [component]
        (is (not (nil? (.queryByLabelText component "City"))))
        (is (not (nil? (.queryByLabelText component "Country"))))
        (is (not (nil? (.queryByTestId component "search-form__search-btn"))))))))
