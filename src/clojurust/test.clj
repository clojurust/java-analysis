(ns clojurust.test)

(require '[clojure.reflect :as r])
(require '[clojure.pprint :as p])
;;; (import org.reflections.reflections Reflection)

(comment (p/pprint (r/reflect java.lang.reflect.Field)))

(defn p [obj] (p/pprint obj))
(defn pt [obj] (p/print-table obj))

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
   (r/reflect obj)})


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