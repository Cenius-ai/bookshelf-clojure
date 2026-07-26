(ns bookshelf.seed
  "Idempotent seed script — populates the database with sample books,
   then exits. Safe to run multiple times."
  (:require [bookshelf.db :as db])
  (:gen-class))

(def sample-books
  [["The Midnight Library"   "Matt Haig"]
   ["Project Hail Mary"      "Andy Weir"]
   ["Klara and the Sun"      "Kazuo Ishiguro"]
   ["The Vanishing Half"     "Brit Bennett"]
   ["Circe"                  "Madeline Miller"]
   ["Piranesi"               "Susanna Clarke"]
   ["Sea of Tranquility"     "Emily St. John Mandel"]
   ["Tomorrow, and Tomorrow, and Tomorrow" "Gabrielle Zevin"]])

(defn -main
  [& _args]
  (println "Seeding database …")
  (let [ds (db/get-ds)]
    (db/init-db! ds)
    (let [existing (db/book-count ds)]
      (if (pos? existing)
        (println (str "Database already has " existing " book(s) — skipping seed."))
        (do
          (doseq [[title author] sample-books]
            (db/insert-book! ds title author)
            (println (str "  + " title " by " author)))
          (println (str "Seeded " (count sample-books) " books."))))))
  (println "Seed complete.")
  (System/exit 0))
