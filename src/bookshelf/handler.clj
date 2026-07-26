(ns bookshelf.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as response]
            [bookshelf.db :as db]
            [bookshelf.views :as views]))

;; ---------------------------------------------------------------------------
;; Helpers
;; ---------------------------------------------------------------------------

(defn- parse-add-form
  "Extracts and trims title and author from a Ring form-params map."
  [params]
  {:title  (some-> (get params "title") str clojure.string/trim)
   :author (some-> (get params "author") str clojure.string/trim)})

(defn- validate-book
  "Returns a seq of error strings, or nil when valid."
  [{:keys [title author]}]
  (let [errs (cond-> []
               (clojure.string/blank? title)  (conj "Title is required.")
               (clojure.string/blank? author) (conj "Author is required."))]
    (when (seq errs) errs)))

;; ---------------------------------------------------------------------------
;; Routes
;; ---------------------------------------------------------------------------

(defroutes app-routes
  ;; GET / — book list
  (GET "/" []
    (let [ds    (db/get-ds)
          books (db/all-books ds)]
      (views/book-list-page books)))

  ;; GET /health — trivial health check
  (GET "/health" []
    {:status 200 :headers {"Content-Type" "text/plain"} :body "ok"})

  ;; GET /add — add-book form
  (GET "/add" []
    (views/add-book-page))

  ;; POST /add — create a book, then redirect
  (POST "/add" req
    (let [form   (parse-add-form (:form-params req))
          errors (validate-book form)]
      (if errors
        (views/add-book-page :errors errors :values form)
        (do
          (db/insert-book! (db/get-ds) (:title form) (:author form))
          (response/redirect "/")))))

  ;; 404
  (route/not-found "Page not found."))

(defn make-handler
  "Returns the Ring handler with params and static-file middleware."
  []
  (-> app-routes
      wrap-params
      (wrap-resource "public")))
