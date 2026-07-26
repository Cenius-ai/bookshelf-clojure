(ns bookshelf.views
  (:require [hiccup.page :refer [html5 include-css]]
            [hiccup.form :as f]))

;; ---------------------------------------------------------------------------
;; Design tokens — keep in sync with resources/public/css/style.css
;; ---------------------------------------------------------------------------

(def accent-hex "#009662")

;; ---------------------------------------------------------------------------
;; Shared layout
;; ---------------------------------------------------------------------------

(defn- layout
  "Wraps body content in the standard HTML chrome."
  [title & body]
  (html5
   {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:title (str title " — Bookshelf")]
    (include-css "/fonts/fonts.css")
    (include-css "/css/style.css")]
   [:body
    [:div.app
     [:header.app-header
      [:a.app-logo {:href "/"} "📚 Bookshelf"]
      [:nav.app-nav
       [:a {:href "/"} "Library"]
       [:a {:href "/add"} "Add a book"]]]
     [:main.app-main
      body]]
    [:footer.app-footer
     [:p "A quiet corner for your reading life."]]]))

;; ---------------------------------------------------------------------------
;; Book list page  —  /
;; ---------------------------------------------------------------------------

(defn book-list-page
  "Renders the full book-list page with a split-pane layout:
   a scrollable library on the left, a summary card on the right."
  [books]
  (layout "Library"
    [:div.split-pane
     ;; ── Left panel: book list ────────────────────────────
     [:section.library-panel
      [:h2.library-heading "Your library"]
      (if (seq books)
        [:ul.book-list
         (for [b books]
           [:li.book-card
            [:span.book-title (:books/title b)]
            [:span.book-author (:books/author b)]])]
        [:div.empty-state
         [:p "No books yet. Why not add one?"]
         [:a.btn.btn-primary {:href "/add"} "Add your first book"]])]

     ;; ── Right panel: quick-add CTA ───────────────────────
     [:aside.detail-panel
      [:div.detail-card
       [:h3 "Build your shelf"]
       [:p "Every great collection starts with a single volume. Track the titles that matter to you."]
       [:a.btn.btn-primary {:href "/add"} "＋ Add a book"]
       (when (seq books)
         [:p.detail-stat (str (count books) " book" (when (> (count books) 1) "s") " on your shelf")])]]]))

;; ---------------------------------------------------------------------------
;; Add-book page  —  /add
;; ---------------------------------------------------------------------------

(defn add-book-page
  "Renders the add-book form with optional error feedback."
  [& {:keys [errors values]}]
  (layout "Add a book"
    [:div.form-page
     [:div.form-card
      [:h2.form-heading "Add a book to your shelf"]
      [:p.form-subtitle "Record a title and its author."]

      (when errors
        [:div.form-errors
         [:p "Please fix the following:"]
         [:ul
          (for [e errors]
            [:li e])]])

      (f/form-to {:class "book-form"}
        [:post "/add"]
        [:div.form-group
         (f/label "title" "Title")
         (f/text-field {:placeholder "e.g. The Midnight Library"
                        :required true
                        :autofocus true}
                       "title"
                       (:title values))]
        [:div.form-group
         (f/label "author" "Author")
         (f/text-field {:placeholder "e.g. Matt Haig"
                        :required true}
                       "author"
                       (:author values))]
        [:div.form-actions
         [:a.btn.btn-ghost {:href "/"} "Cancel"]
         (f/submit-button {:class "btn btn-primary"} "Save to shelf")])]]))
