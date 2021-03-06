# java-analysis

Decode clojure.lang library and base Java Functions. 

This is part of the [Clojurust](https://github.com/clojurust) project.

This can be extended for any jar and classes in order to have an interface description to automagically make a wrapper for other languages (Clojure, Rust, ...)

The generated files are a hierachy of files in a similar structure as the jar. File are the base Class or Interface with an _.edn_ extension.

$ subclasses are in the file with base class name. For example:

* path/Class.edn
  * Class
  * Class$Subclass1
  * Class$Subclass$Subclass2
  * Class$Subclass3
* path/Class2.edn
  * Class2
  * Class2$Subclass21 
  * Class2$Subclass21$Subclass22 
  * Class2$Subclass31

## TODO

* [X] Read clojure.lang classes and interfaces.
* [X] Get Classes and interfaces infos.
* [X] Get other jars objects Classes and interface.
* [X] Get other jars objects Classes and interface information.
* [X] Recurse until all objects are decoded.
* [ ] Sort all data by files definition for objects and interfaces.
* [ ] Generate description files in a result file hierarchy.

## Usage

TBD

## Options

TBD

## Examples

...

### Bugs

...

## License

MIT License

Copyright (c) 2021 kilroySoft, Ivan Pierre <ivan@kilroysoft.ch>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.