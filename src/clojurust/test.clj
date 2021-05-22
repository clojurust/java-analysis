(ns clojurust.test)

(comment (pprint (reflect java.lang.reflect.Field)))

(defn p [obj] (pprint obj))
(defn pt [obj] (print-table obj))

(def xref {:interface {}
           :abstract {}
           :class {}})

(defn pobj [obj]
  (println "Name")
  (p (.getName (class obj)))
  (println "Fields")
  (p (.getDeclaredFields (obj)))
  (println "Functions")
  (p (.toGenericString (obj))))

(defn po [obj]
  {obj
   (reflect obj)})


;;(println "-----------------------------")
;;(println "Object")
;;(println "------------------------")
;;(println (p (po Object)))
(p (po java.lang.reflect.AnnotatedElement))
;;(println "------------------------")

(p (all-ns))
;;; (Reflection. "java.lang")

;;; (doseq [p (.getURLs (java.lang.ClassLoader/getSystemClassLoader))] (println (.getPath p)))
(p (loaded-libs))

(defn get-name
  [file]
  (io/resource file))

(map get-name (cp/system-classpath))

(import (java.io File))

(import (java.util.jar JarFile JarEntry))

(filter #(pos?
          (index-of (.getPath %) "clojure-"))
        (filter jar-file? (system-classpath)))

(index-of (.getPath (first (filter jar-file? (system-classpath)))) "cider")