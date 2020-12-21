(ns example.db
  (:require [clojure.spec.alpha :as s]))

;; spec of app-db
(s/def ::counter number?)
(s/def ::text-input string?)
(s/def ::key string?)
(s/def ::item string?)
(s/def ::todo-list (s/coll-of
                     (s/keys :req-un [::key ::item])))
(s/def ::app-db
  (s/keys :req-un [::counter ::text-input ::todo-list]))

;; initial state of app-db
(defonce app-db {:counter 0
                 :text-input ""
                 :todo-list []})
