(ns hello-compojure.database
  (:require [clojurewerkz.cassaforte.client :as cassandra]
            [clojure.tools.logging :as log]))

(def database-connection false)

(defn connection
  "Connect to the database described by a vector of addresses"
  ([database-nodes]
   (if database-connection
     database-connection
     ((fn []
        (def database-connection
          (try (cassandra/connect database-nodes "hellocompojure")
               (catch Exception ex
                 (log/error ex "Cannot connect to database")
                 false)))
        database-connection))))
  ([]
   database-connection))
