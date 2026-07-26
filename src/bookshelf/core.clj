(ns bookshelf.core
  (:require [bookshelf.db :as db]
            [bookshelf.handler :as handler]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn -main
  "Starts the Jetty server. Database schema is ensured on every boot."
  [& _args]
  (let [ds (db/get-ds)]
    (db/init-db! ds)
    (println "Database ready."))
  (let [port (try
               (Integer/parseInt (System/getenv "PORT"))
               (catch Exception _ 8080))]
    (println (str "Bookshelf starting on 0.0.0.0:" port))
    (jetty/run-jetty (handler/make-handler)
      {:host "0.0.0.0"
       :port port
       :join? true})))
