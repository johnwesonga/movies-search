(ns movie-search.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [movie-search.events :as events]
   [movie-search.views :as views]
   [movie-search.config :as config]
   [movie-search.routes :as routes]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel-2] root-el)))

(defn init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
