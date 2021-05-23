(ns clojurust.java-analysis
  (:gen-class))

(require '[clojure.reflect :as re])
(require '[clojure.pprint :as pp])
(require '[clojure.string :as string])
(require '[clojure.java.classpath :as cp])
(require '[clojure.java.shell :only [sh]])

(defn p [obj] (pp/pprint obj) obj)

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))


(defn jar [jar-name] (first (filter #(pos-int?
                                      (string/index-of (.getPath %) jar-name))
                                    (filter cp/jar-file? (cp/system-classpath)))))


(defn get-all-classes
  [jar]
  (seq (string/split (:out (sh "jar" "tf" (str jar))) #"\n")))

(defn filter-path
  [classes path]
  (filter #(and
            (string/starts-with? % path)
            (string/ends-with? % ".class")) classes))

(defn decode
  [cl]
  [cl (re/reflect (read-string cl))])

(defn generate
  [classes]
  (map #(decode %) classes))

(defn execute []
  (-> "clojure-"
      jar
      get-all-classes
      (filter-path "clojure/lang/")
      generate))

(p (execute))
