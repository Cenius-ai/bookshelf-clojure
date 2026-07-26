(ns bookshelf.db
  (:require [next.jdbc :as jdbc]))

(def db-spec
  "SQLite database configuration — relative file path."
  {:dbtype "sqlite" :dbname "bookshelf.db"})

;; Singleton pooled datasource — created once, reused across all requests.
(def ^:private ds-delay
  (delay (jdbc/get-datasource db-spec)))

(defn get-ds
  "Returns the shared pooled datasource."
  []
  @ds-delay)

(defn init-db!
  "Creates the books table if it does not already exist."
  [ds]
  (jdbc/execute! ds ["
    CREATE TABLE IF NOT EXISTS books (
      id    INTEGER PRIMARY KEY AUTOINCREMENT,
      title TEXT NOT NULL,
      author TEXT NOT NULL
    )
  "]))

(defn all-books
  "Returns all books ordered by most-recently-added first."
  [ds]
  (jdbc/execute! ds ["SELECT id, title, author FROM books ORDER BY id DESC"]))

(defn insert-book!
  "Inserts a new book row. Returns the generated-keys result."
  [ds title author]
  (jdbc/execute! ds
    ["INSERT INTO books (title, author) VALUES (?, ?)" title author]
    {:return-keys true}))

(defn book-count
  "Returns the number of rows in the books table."
  [ds]
  (:count (first (jdbc/execute! ds ["SELECT count(*) AS count FROM books"]))))
