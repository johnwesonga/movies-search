(ns movie-search.routes
  (:require
    [bidi.bidi :as bidi]
    [pushy.core :as pushy]
    [re-frame.core :refer [dispatch]]))

;; first step is to define the routes we want.
(def routes ["/" {"" :home
                  "movie-details" {
                            ["/" :movie-id] :movie-details}}])

;;parse-url function uses bidi/match-route to turn a URL into a ds
(defn- parse-url [url]
  (bidi/match-route routes url))

;;dispatch-route is called with that structure:
(defn- dispatch-route [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route)) "-panel"))]
    #_(dispatch [:set-active-panel panel-name])
    (dispatch [:set-route {:route matched-route :panel panel-name}])
    ))


;; The app-routes function that used to define functions is replaced by one that sets up pushy:
(defn app-routes []
  (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (partial bidi/path-for routes))
