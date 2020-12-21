(ns example.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-counter
 (fn [db _]
   (:counter db)))

(reg-sub
  :get-item
  (fn [db _]
    (:text-input db)))

(reg-sub
  :get-todo-list
  (fn [db _]
    (:todo-list db)))
