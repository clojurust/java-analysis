(ns clojurust.java-analysis
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.reflect :as re]
            [clojure.pprint :as pp]
            [clojure.set :as set]
            [clojure.string :as string]
            [clojure.java.classpath :as cp]
            [clojure.java.shell :as sh]))

(defn p [obj] (pp/pprint obj) obj)

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))


(defn jar
  [jar-name]
  (first (filter #(pos-int?
                   (string/index-of (.getPath %) jar-name))
                 (filter cp/jar-file? (cp/system-classpath)))))

(defn get-all-classes
  [jar-name]
  (seq (string/split (:out (sh/sh "jar" "tf" (str jar-name))) #"\n")))

(defn filter-path
  [classes path]
  (filter #(and
            (string/starts-with? % path)
            (string/ends-with? % ".class")) classes))

(defn trim-class
  [cl]
  (-> cl
      (string/replace ".class" "")
      (string/replace "/" ".")
      ;; (string/replace "$" ".")
      ))

(defn decode
  [cl]
  [(trim-class cl) (re/reflect (eval (Class/forName (trim-class cl))))])

(defn generate
  [classes]
  (into {} (map #(decode %) classes)))

(defn loop-obj
  [class-map]
  (loop [class-ref (vals class-map)
         objs #{}]
    (if-let [cl (first class-ref)]
      (do
        (loop [elems (seq (:members cl))
               objs objs]
          (if-let [elem (first elems)]
            (let [ret (:return-type elem)
                  ret (if ret #{ret} #{})
                  param (into #{} (:parameter-types elem))
                  except (into #{} (:exception-types elem))]
              (p (:name elem))
              (recur (next elems) (set/union objs ret param except)))))
        (recur (next class-ref) objs))
      (set/difference objs (into #{} (keys class-map))))))


(defn execute
  [& {:keys [jar-name path cl]
      :or {jar-name "clojure-" path "clojure/lang/" cl ""}
      :as args}]
  (println args)
  (->
   jar-name
   jar
   get-all-classes
   (filter-path (str path cl))
   generate
   loop-obj))

(comment
  (execute
   :jar-name "clojure-"
   :path "clojure/lang/"
   :cl "APersistentMap")
  (execute
   :jar-name "clojure-"
   :path "clojure/lang/")
  (spit "res.edn" (execute))
  (clojure.pprint/pprint (execute) (io/writer "res.edn"))
  ;;
  )
