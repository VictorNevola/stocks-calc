(ns clojure-capital-gains.core
  (:require [clojure-capital-gains.controller.reader-file :as controller]
            [clojure-capital-gains.service.operation :as service]))

(->>  (controller/reader-file) ;; controller to read file
      (service/handler-operations) ;; service to handle operations and calculate taxes
      (controller/output-json) ;; controller to output json
     )
