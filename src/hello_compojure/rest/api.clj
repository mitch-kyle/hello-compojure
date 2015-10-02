(ns hello-compojure.rest.api
  (:require [compojure.core :refer :all]
            [ring.util.response :refer [response]]
            [hello-compojure.rest.comments :refer [comment-routes]]))

(defroutes api-routes
  (context "/comment" []
           comment-routes))
