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

(re-frame/reg-sub
  ::active-panel
  (fn [db]
    (get-in db [:route :panel])))

(re-frame/reg-sub
  ::route-params
  (fn [db]
    (get-in db [:route :route :route-params])))

(re-frame/reg-sub
  ::movie-id
  (fn [db]
    (:movie-id db)))

(re-frame/reg-sub
  ::movie
  (fn [db [_ movie-id]]
    (first (filter #(= (int movie-id) (:id %)) (:movies db)))))

