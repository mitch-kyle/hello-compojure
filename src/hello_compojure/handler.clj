(ns hello-compojure.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [redirect]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [hello-compojure.database :as database]
            [hello-compojure.rest.api :refer [api-routes]]))

(database/connection ["127.0.0.1"])


(defroutes app-routes
  (GET "/" [] (redirect "/index.html"))
  (context "/api" [] api-routes)
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-json-response)
      (wrap-resource "public")
      (wrap-defaults api-defaults)))
