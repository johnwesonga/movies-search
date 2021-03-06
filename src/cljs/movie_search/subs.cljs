(ns movie-search.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::movies
 (fn [db]
   (:movies db)))

(re-frame/reg-sub
 ::query
 (fn [db]
   (:query db)))


(re-frame/reg-sub
 ::error-message
 (fn [db]
   (:error-message db)))