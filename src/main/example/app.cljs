(ns example.app
  (:require
    ["react-native" :as rn]
    [reagent.core :as r]
    [re-frame.core :as rf]
    [shadow.expo :as expo]
    [example.events]
    [example.subs]))

;; must use defonce and must refresh full app so metro can fill these in
;; at live-reload time `require` does not exist and will cause errors
;; must use path relative to :output-dir

(defonce splash-img (js/require "../assets/shadow-cljs.png"))

(def styles
  {:container        {}
   :input-container  {:flex-direction  :row
                      :justify-content :center
                      :align-items     :center
                      :margin          50}
   :input            {:flex          1
                      :height        35
                      :border-color  :black
                      :border-width  1
                      :margin-bottom 10}
   :list-container   {:margin 20}
   :button-container {:flex            1
                      :flex-direction  :row
                      :justify-content :space-around
                      :width           "60%"}
   :button           {:width "40%"}
   :item-container   {:margin 5
                      :background-color :pink}})

(defn root []
  [:> rn/View {:style (:container styles)}
   [:> rn/View {:style (:input-container styles)}
    [:> rn/TextInput {:placeholder       "Goal..."
                      :placeholder-color :gray
                      :keyboard          :default
                      :style             (:input styles)
                      :on-change-text    #(rf/dispatch-sync [:change-text %])
                      :value             @(rf/subscribe [:get-item])}]
    [:> rn/View {:style (:button-container styles)}
     [:> rn/Button {:title    "ADD"
                    :color    :blue
                    :style    (:button styles)
                    :on-press #(rf/dispatch [:add-item-to-list])}]
     [:> rn/Button {:title    "CANCEL"
                    :color    :red
                    :style    (:button styles)
                    :on-press #(rf/dispatch [:clear-text])}]]]
   [:> rn/View {:style (:list-container styles)}
    [:> rn/FlatList {:data        @(rf/subscribe [:get-todo-list])
                     :render-item (fn [obj]
                                    (let [obj (.-item obj)
                                          item (get obj "item")
                                          key (get obj "key")]
                                      (r/as-element
                                        [:> rn/View {:style (:item-container styles)}
                                         [:> rn/Pressable {:on-press #(rf/dispatch [:delete-item key])}
                                          [:> rn/Text item]]])))}]]])

(defn start
  {:dev/after-load true}
  []
  (expo/render-root (r/as-element [root])))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (start))
