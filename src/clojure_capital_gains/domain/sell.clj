(ns clojure-capital-gains.domain.sell)

(def min-loss-profit 0.00)
(def min-stock-quantity 0)
(def min-amount-operation-to-tax 20000)
(def min-fee-tax 0.00)
(def tax-fee 0.20)

(defn calculate-new-stock-quantity [state operation] "calculate new quantity"
  (let [actual-quantity (:cumulative-stock-quantities state)
        quantity (:quantity operation)
        new-quantity (max min-stock-quantity (- actual-quantity quantity))]
    new-quantity))

(defn calculate-profit [state operation] "calculate profit"
  (let [op-cost (:unit-cost operation)
        op-stocks (:quantity operation)
        weighted-mean (:weighted-mean-amount state)
        profit (-> (- op-cost weighted-mean)
                   (* op-stocks))]
    profit)
  )

(defn calculate-loss [state operation] "calculate loss when sell stocks"
  (let [operation-profit (calculate-profit state operation)
        actual-loss (:cumulative-loss state)]

      (if (< operation-profit min-loss-profit)
        (+ (Math/abs (double operation-profit)) actual-loss)
        (max min-loss-profit (- actual-loss operation-profit)))
    )
  )

(defn calculate-tax [state operation] "calculate tax when buy new stocks"
  (let [op-cost (:unit-cost operation)
        op-stocks (:quantity operation)
        operation-profit (calculate-profit state operation)
        operation-amount (* op-cost op-stocks)
        operation-profit-discounted-loss (- operation-profit (:cumulative-loss state))]

    (if (or (<= operation-amount min-amount-operation-to-tax)
            (<= operation-profit-discounted-loss min-loss-profit))
      min-fee-tax
      (* operation-profit-discounted-loss tax-fee)
      ))
  )
