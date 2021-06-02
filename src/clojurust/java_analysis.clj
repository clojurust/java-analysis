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

(declare analysis)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (analysis :jar-name (first args) :path (first args) :cl (first args)))


(defn jar
  "Get Jar from name or part of name"
  [jar-name]
  (first (->>
          ;;; Filter all jars in classpath
          (filter cp/jar-file?
                  (cp/system-classpath))
          ;;; Filter with `jar-name` to get the correct jar
          (filter #(pos-int?
                    (string/index-of (.getPath %) jar-name)))
          p)))

(defn get-all-classes
  "Call `jar tf` to get jar content as sequence of lines"
  [jar-name]
  (seq (string/split (:out (sh/sh "jar" "tf" (str jar-name))) #"\n")))

(defn filter-path
  "Suppress non classes and `classes` not in `path` if the `jar tf` file"
  [lines path]
  (filter #(and
            (string/starts-with? % path)
            (string/ends-with? % ".class")) lines))

(defn trim-class
  "Suppress `.class` and change `/` into . to correspond to Clojure class name"
  [cl]
  (-> cl
      (string/replace ".class" "")
      (string/replace "/" ".")))

(defn decode
  "Get all reflection data for one class"
  [cl]
  (let [cl (trim-class cl)]
    [cl
     (-> cl
         Class/forName
         eval
         re/reflect)]))

(defn generate
  "Get all reflection data of all `classes`"
  [classes]
  (into {} (map #(decode %) classes)))

(defn loop-members
  "Manage members of class to find undefined objects"
  [cl objs keys-names]
  (loop [elems (seq (:members cl))
         objs objs]
    (if-let [elem (first elems)]
      (let [ret (:return-type (str elem))
            ret (if ret #{ret} #{})
            param (into #{} (map str (:parameter-types elem)))
            except (into #{} (map str (:exception-types elem)))]
        (str :ret ret :param param :except except)
        (recur (next elems) (set/difference (set/union objs ret param except) keys-names)))
      objs)))

(defn loop-obj
  "Walk through classes to find undefined objects in members"
  [class-map]
  (let [keys-names (into #{} (keys class-map))]
    (loop [class-ref (seq class-map)
           objs #{}]
      (if-let [cl (first class-ref)]
        (let [objs (set/union objs (loop-members (cl 1) objs keys-names))]
          (println (cl 0))
          (recur (next class-ref) objs))
        (set/difference objs keys-names)))))

(defn execute
  "Main execution of analysis acording to optionals `jar-name`, `path` in the jar and `class`"
  [jar-name path cl]
  (println jar-name path cl)
  (->
   jar-name
   p
   jar
   p
   get-all-classes
   (filter-path (str path cl))
   generate
   loop-obj
   p)
  nil)

(defn analysis
  "Callable entry point to the application."
  [& {:keys [jar-name path cl]
      :or {jar-name "clojure-" path "clojure/lang/" cl ""}
      :as args}]
  (println args jar-name path cl)
  (execute jar-name path cl))

(comment
  ;; get info only on APersistantMap
  (analysis :jar-name "clojure-"
            :path "clojure/lang/"
            :cl "APersistentMap")

  ;; Complete analysis of clojure-*.jar
  (analysis)

  ;; Write raw analysis of clojure-*.jar
  (spit "res.edn" (analysis))

  ;; Write fomatted analysis of clojure-*.jar
  (clojure.pprint/pprint (analysis) (io/writer "res.edn"))
  ;;
  )
