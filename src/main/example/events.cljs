(ns example.events
  (:require
    [re-frame.core :refer [reg-event-db after]]
    [clojure.spec.alpha :as s]
    [example.db :as db :refer [app-db]]))

;; -- Interceptors ------------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/master/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db [event]]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check after " event " failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (after (partial check-and-throw ::db/app-db))
    []))

;; -- Handlers --------------------------------------------------------------

(reg-event-db
  :initialize-db
  validate-spec
  (fn [_ _]
    app-db))

(reg-event-db
  :inc-counter
  validate-spec
  (fn [db [_ _]]
    (update db :counter inc)))

(reg-event-db
  :change-text
  validate-spec
  (fn [db [_ s]]
    (assoc db :text-input s)))

(reg-event-db
  :clear-text
  validate-spec
  (fn [db [_ _]]
    (assoc db :text-input "")))

(reg-event-db
  :add-item-to-list
  validate-spec
  (fn [db [_ _]]
    (let [item (:text-input db)]
      (if (seq item)
        (update db :todo-list #(conj % {:key (str (random-uuid)) :item item}))
        db))))

(reg-event-db
  :delete-item
  validate-spec
  (fn [db [_ k]]
    (update db :todo-list (fn [items] (filter #(not= k (:key %)) items)))))
