(ns clojure-capital-gains.controller.reader-file
  (:require [clojure.string :as str]
            [clojure.data.json :as json]
            [clojure-capital-gains.domain.operation :as domain-operation]))

(defn split-operations [file-content]
  (->> (str/split file-content #"(?<=\])\s*(?=\[)")
       (map str/trim)
       (map json/read-str))
  )

(defn reader-file [] "Read file and return content in operation data"
  (->>  (slurp "resources/test-operations.txt")
        (split-operations)
        (map #(map domain-operation/new-operation %))
       )
  )

(defn output-json
  "Output json string"
  [taxes]
  (->> taxes
       (run! #(println (json/write-str %)))
       )
  )