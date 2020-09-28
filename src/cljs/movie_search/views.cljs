(ns movie-search.views
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [movie-search.subs :as subs]
   [reagent.core  :as reagent]
   [cljs.pprint :refer [pprint]]
   ))

(defn search-movies 
[event search-param]
  (.preventDefault event)
 (dispatch [:search-movies search-param])
)
(defn movie-item
 [{:keys [title poster_path release_date vote_average overview]}]
  [:div.card
    [:img.card--image 
      {:src (str "https://image.tmdb.org/t/p/w185_and_h278_bestv2" poster_path) 
       :alt (str title " poster")}]
    [:div.card--content
      [:h3.card--title title]
      [:p [:small (str "RELEASE DATE: " release_date)]]
      [:p [:small (str "RATING: " vote_average)]]
      [:p.card--desc overview]
    ]
    ])

(defn movies-list 
[results]
[:div
  (if (empty? results)
   [:div.card-list
     [:p "No movie"]]
    (for [movie results]
      ^{:key (:poster_path movie)} [movie-item movie]))])

(defn search-movies-form 
  []
    (let [default ""
          search-param (reagent/atom default)]
      (fn []
        (let [query @search-param]
          [:div.container
          [:h1.title "Re-frame Movie Search"]
          [:form {:class "form" :on-submit #(search-movies % @search-param)}
            [:label {:class "label"} "Movie Name"]
            [:input {:class "input" 
                    :type :text 
                    :name "query" 
                    :value query
                    :on-change #(reset! search-param  (-> % .-target .-value))
                    :placeholder "i.e. Jurassic Park"}]
            [:button {:class "button" 
                    :type :submit 
                    } "Search"]]]))))

(defn main-panel []
  (let [name (subscribe [::subs/name])
        movies (subscribe [::subs/movies])
        query (subscribe [::subs/query])]
    [:div
      [search-movies-form]
      (if-not (empty? @query)
        [movies-list @movies])
      
     ]))
