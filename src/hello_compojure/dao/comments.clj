(ns hello-compojure.dao.comments
  (:require [hello-compojure.database :as database]
            [clojure.tools.logging :as log]
            [clojurewerkz.cassaforte.query :refer [where]]
            [clojurewerkz.cassaforte.cql :as cql]
            [clojurewerkz.cassaforte.uuids :as uuid]))

;; Sanitized CRUD access to the comments table

(def table-name "comments")

(defn- wrap-error-logger
  "Logs errors from the database and returns false if the operation cannot be completed"
  [callback]
  (if (database/connection)
    (try (callback)
         (catch Exception ex
           (log/error ex)
           false))
    ((fn []
       (log/error "Database connection not initialized")
       false))))

(defn create-comment
  "Create a comment"
  [{author :author body :body}]
  (wrap-error-logger (fn []
                       (cql/insert (database/connection)
                                   table-name
                                   {:id (uuid/time-based)
                                    :author author
                                    :body body
                                    :posted (.getTime (new java.util.Date))}))))

(defn retrieve-comment
  "Retrieve a comment or list of comments"
  ([id]
   (wrap-error-logger (fn []
                       (cql/select (database/connection)
                                   table-name
                                   (where [[= :id id]])))))
  ([]
   (wrap-error-logger (fn []
                       (cql/select (database/connection)
                                   table-name)))))
  

(defn update-comment
  "Update a comment"
  [data]
  (wrap-error-logger (fn [] 
                      (cql/update (database/connection)
                                  table-name
                                  (where [[= :id (:id id)]])
                                  (merge  {:author (:author data)}
                                          {:body (:body data)}
                                          {:posted (.getTime (new java.util.Date))})))))

(defn delete-comment
  "Delete a comment"
  [id]
  (wrap-error-logger (fn [] 
                      (cql/delete (database/connection)
                                  table-name
                                  (where [[= :id (java.util.UUID/fromString id)]])))))
