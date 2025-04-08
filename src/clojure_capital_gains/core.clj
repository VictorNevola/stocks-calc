(ns clojure-capital-gains.core
  (:require [clojure.string :as str]
            [clojure.data.json :as json]))

(defn read-file [file-path]
  (slurp file-path))

(defn split-operations [file-content]
  (->> (str/split file-content #"(?<=\])\s*(?=\[)")
       (map str/trim)
       (map json/read-str)))

(defn calc-weighted-mean [operation state]
    (let [stocks (:cumulative-stock-quantities state)
          weighted-mean (:weighted-mean-amount state)
          op-stocks (get operation "quantity")
          op-cost (get operation "unit-cost")
          new-weighted-mean (-> (+ (* stocks weighted-mean) (* op-stocks op-cost))
                                (/ (+ stocks op-stocks)))]
      new-weighted-mean)
  )

(defn calc-profit [operation state]
    (let [op-cost (get operation "unit-cost")
          op-stocks (get operation "quantity")
          weighted-mean (:weighted-mean-amount state)
          profit (-> (- op-cost weighted-mean)
                     (* op-stocks))
          ]profit)
  )

(defn calc-fee-tax [operation operation-profit cumulative-loss]
  (let [op-cost (get operation "unit-cost")
        op-stocks (get operation "quantity")
        operation-amount (* op-cost op-stocks)
        operation-profit (- operation-profit cumulative-loss)]

    (if (or (<= operation-amount 20000)
            (<= operation-profit 0.00))
      0.00 (* operation-profit 0.23)
      ))
  )

(defn set-cumulative-loss [operations-profit, actual-loss]
    (if (< operations-profit 0.00)
      (+ (Math/abs (double operations-profit)) actual-loss)
      (max 0.00 (- actual-loss operations-profit)))
  )

(defn calc-tax [operations]
  (let [initial-state {:weighted-mean-amount 0.00
                       :cumulative-stock-quantities 0.00
                       :cumulative-loss 0.00
                       :taxes []}

        result (reduce (fn [state operation]
                         (cond
                           (= (get operation "operation") "buy")
                            (-> state
                                (assoc  :weighted-mean-amount (calc-weighted-mean operation state))
                                (assoc  :cumulative-stock-quantities (+ (:cumulative-stock-quantities state) (get operation "quantity")))
                                (update :taxes conj {:tax 0.00}))
                            (= (get operation "operation") "sell")
                            (-> state
                                (assoc :cumulative-loss (set-cumulative-loss (calc-profit operation state) (:cumulative-loss state)))
                                (assoc :cumulative-stock-quantities (max 0 (- (:cumulative-stock-quantities state) (get operation "quantity"))))
                                (update :taxes conj {:tax (calc-fee-tax operation (calc-profit operation state) (:cumulative-loss state))}))
                           ))
                       initial-state
                       operations
                       )] (:taxes result)))

(->> (read-file "resources/test-operations.txt")
     (split-operations)
     (map #(calc-tax %))
     (run! #(println (json/write-str %))))
