(ns clojure-capital-gains.core
  (:require [clojure.string :as str]
            [clojure.data.json :as json]
            [clojure-capital-gains.domain.operation :as domain-operation]
            [clojure-capital-gains.domain.buy :as domain-buy]
            [clojure-capital-gains.domain.sell :as domain-sell]))

(defn read-file [file-path]
    (slurp file-path)
  )

(defn split-operations [file-content]
  (->> (str/split file-content #"(?<=\])\s*(?=\[)")
       (map str/trim)
       (map json/read-str))
  )

(defn handler-operations [operations]
  "Handle operations by groups and calculate fee tax"
  (let [initial-state (domain-operation/new-state)
        result (reduce (fn [state operation]
                         (cond
                           (= (:type operation) domain-operation/type-operation-buy)
                             (-> state
                                  (assoc  :weighted-mean-amount (domain-buy/calculate-weighted-mean state operation))
                                  (assoc  :cumulative-stock-quantities (domain-buy/calculate-new-stock-quantity state operation))
                                  (update :taxes conj {:tax (domain-buy/calculate-tax)})
                                 )
                           (= (:type operation) domain-operation/type-operation-sell)
                            (-> state
                                  (assoc :cumulative-loss (domain-sell/calculate-loss state operation))
                                  (assoc :cumulative-stock-quantities (domain-sell/calculate-new-stock-quantity state operation))
                                  (update :taxes conj {:tax (domain-sell/calculate-tax state operation)})
                                )
                           )
                         ) initial-state operations)]
    result))

(->> (read-file "resources/test-operations.txt")
      (split-operations)
      (map #(map domain-operation/new-operation %))
      (map #(handler-operations %))
      (run! #(println (json/write-str (:taxes %))))
     )
