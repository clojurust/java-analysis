# java-analysis
Analyze java libraries structure and generate reflection data

## Functions
This Clojure program will analyse Classes and Interface to generate an EDN data representing them in a structured form. It will recursively manage all of hist elements and take other Classes and Interfaces used to define them. 

The process stops when all data has been generated.

Main function manages only a file, but a function can manage all files of a namespace. 
