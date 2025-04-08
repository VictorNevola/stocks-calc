(ns clojure-capital-gains.service.operation
  [:require [clojure-capital-gains.domain.operation :as domain-operation]
            [clojure-capital-gains.domain.buy :as domain-buy]
            [clojure-capital-gains.domain.sell :as domain-sell]])

(defn calculate-taxes-to-buy
  "calculate taxes to buy operation"
  [state operation]
  (-> state
      (assoc  :weighted-mean-amount (domain-buy/calculate-weighted-mean state operation))
      (assoc  :cumulative-stock-quantities (domain-buy/calculate-new-stock-quantity state operation))
      (update :taxes conj {:tax (domain-buy/calculate-tax)})
      )
  )

(defn calculate-taxes-to-sell
  "calculate taxes to sell operation"
  [state operation]
  (-> state
        (assoc :cumulative-loss (domain-sell/calculate-loss state operation))
        (assoc :cumulative-stock-quantities (domain-sell/calculate-new-stock-quantity state operation))
        (update :taxes conj {:tax (domain-sell/calculate-tax state operation)})
      )
  )

(defn calc-operations [operations]
  "Calc operations by groups to calculate fee tax"
  (let [initial-state (domain-operation/new-state)
        result (reduce (fn [state operation]
                         (cond
                            (= (:type operation) domain-operation/type-operation-buy)  (calculate-taxes-to-buy state operation)
                            (= (:type operation) domain-operation/type-operation-sell) (calculate-taxes-to-sell state operation)
                           )
                         ) initial-state operations)]
    result))

(defn handler-operations
  "handler operations each group and return taxes"
  [operations]
  (->> operations
        (map #(calc-operations %))
        (map #(:taxes %))
       )
  )
