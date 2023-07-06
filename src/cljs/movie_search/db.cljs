(ns movie-search.db
  (:require
    [cljs.spec.alpha :as s]))

(s/def ::query string?)
(s/def ::error-message string?)

(def default-db
  {
    :movies []
    :query nil
    :error-message nil
    :movie-id nil
    :route nil})
