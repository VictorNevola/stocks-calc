(ns clojure-capital-gains.domain.buy)

(def fee-tax 0.00)

(defn calculate-weighted-mean [state operation] "calculate weighted mean when buy new stocks"
  (let [stocks (:cumulative-stock-quantities state)
        weighted-mean (:weighted-mean-amount state)
        op-stocks (:quantity operation)
        op-cost (:unit-cost operation)
        new-weighted-mean (-> (+ (* stocks weighted-mean) (* op-stocks op-cost))
                              (/ (+ stocks op-stocks)))]
    new-weighted-mean)
  )

(defn calculate-new-stock-quantity [state operation] "calculate new quantity"
  (let [actual-quantity (:cumulative-stock-quantities state)
        quantity (:quantity operation)
        new-quantity (+ actual-quantity quantity)]
    new-quantity))

(defn calculate-tax [] "calculate tax when buy new stocks" fee-tax)
