(ns events.component.pagination-test
  (:require [events.component.pagination :refer [pagination]]
            [events.test-helpers :as helpers]
            [clojure.string :as str]
            [cljs.test :refer [deftest is testing use-fixtures]]
            ["@testing-library/react" :as react]))

(deftest smoke
  (testing "pagination isn't displayed if there is no page to show"
    (helpers/with-mounted-component
      [pagination
       {:page {:total-pages 0, :number 0}
        :on-goto-page identity}]
      #(is (helpers/element-doesnt-exist-by-role % "pagination"))))

  (testing "pagination isn't displayed if there is only one page to show"
    (helpers/with-mounted-component
      [pagination
       {:page {:total-pages 1, :number 0}
        :on-goto-page identity}]
      #(is (helpers/element-doesnt-exist-by-role % "pagination")))))

(deftest navigation-arrows-tests
  (testing "goto previous page is disabled if the current page is the first"
    (helpers/with-mounted-component
      [pagination
       {:page {:total-pages 2, :number 0}
        :on-goto-page identity}]
      #(let [el (.getByRole % "pagination__previous")]
         (is (str/includes? (.-className el) "disabled")))))

  (testing "goto next page is disabled if the current page is the last"
    (helpers/with-mounted-component
      [pagination
       {:page {:total-pages 2, :number 1}
        :on-goto-page identity}]
      #(let [el (.getByRole % "pagination__next")]
         (is (str/includes? (.-className el) "disabled")))))

  (testing "both `goto` page commands are enabled if the current page is not the first or the last"
    (helpers/with-mounted-component
      [pagination
       {:page {:total-pages 3, :number 1}
        :on-goto-page identity}]
      #(let [previous (.getByRole %1 "pagination__previous")
             next (.getByRole %1 "pagination__next")]
         (is (not (str/includes? (.-className previous) "disabled")))
         (is (not (str/includes? (.-className next) "disabled")))))))

(deftest on-goto-page-tests
  (testing "event is fired with correct page number"
    (let [pn (atom nil)]
      (helpers/with-mounted-component
        [pagination
         {:page {:total-pages 3, :number 1}
          :on-goto-page #(reset! pn %)}]
        #(fn [component]
           (let [previous (.getByRole component "pagination__previous-a")]
             (.click react/fireEvent previous)
             (is (= 0 @pn)))

           (let [next (.getByRole component "pagination__next-a")]
             (.click react/fireEvent next)
             (is (= 2 @pn)))

           (let [pages (.getAllByRole component "pagination__page")]
             (doseq [i (range 0 (count pages))]
               (.click react/fireEvent (nth pages i))
               (is (= i @pn)))))))))
