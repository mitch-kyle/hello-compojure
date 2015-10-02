(ns hello-compojure.rest.comments
  (:require [compojure.core :refer :all]
            [clojure.tools.logging :as log]
            [ring.util.response :refer [response status]]
            [hello-compojure.dao.comments :as dao]))

(defn- wrap-error-handler
  "Returns error response if operation fails"
  [operation failure-msg]
  (let [result (operation)]
    (if result
      (response result)
      ((fn []
         (log/error "Could not complete request")
         (status (response failure-msg) 500))))))
  
(defn- wrap-hide-output
  "Performs operation and returns success-msg if it succeeds"
  [operation success-msg]
  (let [result (operation)]
    (if result
      success-msg
      ((fn []
         (log/error result "Could not complete request")
         false)))))

(defroutes comment-routes
  (GET "/"
       []
       (wrap-error-handler dao/retrieve-comment {:error "failed to retieve comments"}))
  (POST "/"
        {{author :author body :body} :body}
        (wrap-error-handler (fn []
                              (wrap-hide-output (fn []
                                                  (dao/create-comment {:author author :body body}))
                                                  {:message "Successfully created comment"}))
                              {:error "Could not create comment"}))
  (context "/:id" [id] (routes
                        (GET "/"
                             []
                             (wrap-error-handler (fn []
                                                   (dao/retrieve-comment id))
                                                 {:error "failed to retrieve comment"}))
                        (DELETE "/"
                                []
                                (wrap-error-handler (fn []
                                                      (wrap-hide-output (fn []
                                                                          (dao/delete-comment id))
                                                  {:message "Successfully deleted comment"}))
                                                    {:error "Could not delete comment"}))
                        (PUT "/"
                             {{author :author body :body} :body}
                             (wrap-error-handler (fn []
                                                   (wrap-hide-output (fn []
                                                 (dao/update-comment {:id id :author author :body body}))
                                                                     {:message "Successfully updated comment"}))
                                                 {:error "Could not update comment"})))))
  
