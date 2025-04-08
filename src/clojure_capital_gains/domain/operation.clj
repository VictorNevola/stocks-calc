(ns clojure-capital-gains.domain.operation)

(def initial-weighted-mean-amount 0.00)
(def initial-cumulative-stock-quantities 0.00)
(def initial-cumulative-loss 0.00)
(def initial-taxes [])
(def type-operation-buy :buy)
(def type-operation-sell :sell)

(defn new-operation [operation] "define new operation with correctly type"
  {:quantity (get operation "quantity")
   :unit-cost (get operation "unit-cost")
   :type (keyword (get operation "operation"))}
  )

(defn new-state [] "define new initial state"
  {:weighted-mean-amount initial-weighted-mean-amount
   :cumulative-stock-quantities initial-cumulative-stock-quantities
   :cumulative-loss initial-cumulative-loss
   :taxes initial-taxes}
  )
