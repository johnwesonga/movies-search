(ns movie-search.views
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [movie-search.subs :as subs]
   [reagent.core  :as reagent]
   [cljs.pprint :refer [pprint]]
   [movie-search.routes :as routes]
   ))

(defn search-movies 
  [event search-param]
    (.preventDefault event)
    (dispatch [:search-movies search-param]))

(defn movie-item
 [{:keys [id title poster_path release_date vote_average overview]}]
  [:div.card
    [:img.card--image 
      {:src (str "https://image.tmdb.org/t/p/w185_and_h278_bestv2" poster_path) 
       :alt (str title " poster")}]
    [:div.card-body
      [:h3.card-title title]
      [:p.card-text (str "RELEASE DATE: " release_date)]
      [:p.card-text (str "RATING: " vote_average)]
      [:p.card-text overview]
      [:a {:href (routes/url-for :movie-details :movie-id id) :class "btn btn-primary"} "more"]]])

(defn movies-list 
  [results]
    (let [error-message (subscribe [::subs/error-message])]
      [:div
      (if (empty? results)
        [:div.card-list
         [:p @error-message]]
          (for [movie results]
            ^{:key (:poster_path movie)} [movie-item movie]))]))

(defn movie-details-panel []
  (let [route-params (subscribe [::subs/route-params])
        movie-id (:movie-id @route-params)
        movie (subscribe [::subs/movie movie-id])]
    [:div
     [:ul {:class "nav"}
      [:li.nav-item
       [:a {:class "nav-link active" :aria-current "page" :href "/"} "Home"]]]
     [movie-item @movie]
     ]))

(defn search-movies-form 
  []
    (let [default ""
          search-param (reagent/atom default)]
      (fn []
        (let [query @search-param]
          [:div
          [:h1 {:class "title"} "Re-frame Movie Search"]
          [:form {:class "form" :on-submit #(search-movies % query)}
            [:label {:class "label"} "Movie Name"]
            [:input {:class "input" 
                    :type :text 
                    :name "query" 
                    :value query
                    :on-change #(reset! search-param  (-> % .-target .-value))
                    :placeholder "i.e. Jurassic Park"}]
            [:button {:class "button" :type :submit} "Search"]]]))))

(defn search-movies-panel []
  (let [movies (subscribe [::subs/movies])
        query (subscribe [::subs/query])
        active-panel (subscribe [::subs/active-panel])]
    [:div
     [:pre (with-out-str (pprint @active-panel))]
     [search-movies-form]
     (if-not (empty? @query)
       [movies-list @movies])
     ]))

(defmulti panels identity)
(defmethod panels :movie-details-panel [] [movie-details-panel])
(defmethod panels :default [] [search-movies-panel])



(defn main-panel []
  (let [movies (subscribe [::subs/movies])
        query (subscribe [::subs/query])
        active-panel (subscribe [::subs/active-panel])]
    [:div
      [search-movies-form]
      (if-not (empty? @query)
        [movies-list @movies])
     ]))

(defn main-panel-2 []
  (let [active-panel (subscribe [::subs/active-panel])]
    (fn []
      [panels @active-panel])))


