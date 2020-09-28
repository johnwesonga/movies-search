(ns movie-search.events
  (:require
   [re-frame.core :as re-frame]
   [movie-search.db :as db]
    [day8.re-frame.http-fx]
   [ajax.core :as ajax]

   ))

(def api-url "")

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
  :error-message
  (fn [db [_ result]]
    (-> db 
      (assoc :error-message result)
      )))

(re-frame/reg-event-db
  :success-http-result
  (fn [db [_ data]]
    (-> db 
      (assoc :movies (->> data
                         :results
                         (filter #(if-not nil (get-in % [:poster_path])))))
      )))

(re-frame/reg-event-fx
:search-movies
(fn [{:keys [db]} [_ search-param]]
 {:http-xhrio {:method         :get
                  :uri          (str "https://api.themoviedb.org/3/search/movie?api_key=5dcf7f28a88be0edc01bbbde06f024ab&language=en-US&query=" search-param "&page=1&include_adult=false")
                  :timeout       8000 
                  :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                  :on-success      [:success-http-result]
                  :on-failure      [:error-message "Failed to load movies"]}
  :db (assoc db :query search-param)}))