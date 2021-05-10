(ns clojurust.test
  (:require [clojure.reflect :as ref]
            [clojure.pprint :as p]))

(p/pprint (ref/type-reflect java.lang.reflect.Field))

(.getDeclaredFields (class java.lang.reflect.Field))