<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Scala: Getting Started](#scala-getting-started)
  - [Intro](#intro)
    - [Install](#install)
    - [REPL](#repl)
  - [Building Blocks](#building-blocks)
    - [Simple Build Tool (sbt)](#simple-build-tool-sbt)
    - [ScalaTest](#scalatest)
    - [Expressive Clean Code](#expressive-clean-code)
    - [Checking the File System](#checking-the-file-system)
    - [Mapping the Data](#mapping-the-data)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Scala: Getting Started

> My notes from Pluralsight [course](https://app.pluralsight.com/library/courses/scala-getting-started/table-of-contents)

## Intro

### Install

```shell
$ brew update
$ brew install scala
$ brew install sbt
```

### REPL

Just type `scala` in terminal, then to print something to screen:

```scala
scala> println("Hello World")
Hello World
```

Declare a value variable with the name `hello`, type of `String`, and assign it the string "Hello World".
Note that variable type follows the variable name. REPL responds with information about newly created variable,
including name of variable, the variable type, and what that variable is equal to, by evaluating its `toString` method.

```scala
scala> val hello : String  = "Hello World"
hello: String = Hello World

scala> println(hello)
Hello World
```

Can achieve the above with even less boilerplate using Scala's _type inference_.

```scala
scala> val helloInferred = "hello type inference"
helloInferred: String = hello type inference

scala> val myNum = 5
myNum: Int = 5

scala> val MyDbl = 5.5
MyDbl: Double = 5.5
```

__Unified Type System__

![Scala Unified Type System](images/scala-unified-type-system.png "Scala Unified Type System")

All values in Scala are objects, inheriting from `scala.Any`. From there, it splits between value objects `scala.AnyVal` and reference objects `scala.AnyRef`.

wrt inference, Scala tries to find greatest common factor of all items being returned or assigned. For example, a method that returns a `java.lang.String` in a `scala.List`,  then the greatest common factor between those two types is `scala.AnyRef`, which is what the compiler will infer.

If you know the return type in advance, can explicitly annotate it in the code.

Further code simplification, note no semicolon, only needed when performing 2 or more evaluations on the same line of code, otherwise compiler uses whitespace or code block positioning to determine end of expression:

```scala
scala> "Hello REPL"
res2: String = Hello REPL
```

In the above case, the compiler has taken the value "Hello REPL" and automatically assigned it to a system generated value variable `res2`. This is just a nicety for testing code in the REPL.

__Variables__

Note the term _value variable_, not simply _variable_. Will get an error if try to re-assign a value variable such as `hello` from earlier example.

```scala
hello = "Hello"
<console>:12: error: reassignment to val
       hello = "Hello"
             ^
```

Once set, a value variable is _immutable_ and cannot be changed.

For mutable variables, use keyword `var`:

```scala
scala> var mutableVar = 0
mutableVar: Int = 0

scala> var mutableVar = 1
mutableVar: Int = 1
```

However, Scala is _preferentially functional_ and so prefers immutability. Default should be to use `val` over `var`.

__Methods__

Methods are created using `def` keyword. General form of method definition is `def` followed by method name,
then comma separated list of input parameters in parenthesis,
then a colon and return type, which can be ommitted if you want compiler to infer it,
finally `=` followed by method logic.

If code is a simple expression, brackets can be ommitted, otherwise, wrap logic in brackets.

Example with return type inferred, note intentional typo of returning string "0" instead of int 0.
Compiler will accept this and infer Any type <- __danger of type inference__

```scala
scala>def boolToInt(value : Boolean) = if(value) 1 else "0"
boolToInt: (value: Boolean)Any
```

Example with return type explicitly annotated, note had to fix string/int 0 typo otherwise compiler would error on unexpected return type:

```scala
scala> def boolToInt2(value : Boolean): Int = if(value) 1 else 0
boolToInt2: (value: Boolean)Int
```

Note in above method bodies, there's no explicit `return` statement. In Scala, all code is _expression based_. Meaning everything returns something, therefore last item in code evaluation automatically becomes return value.

With `if`, each branch returns a value. If/else in Scala is not standard control flow, but rather an expression that returns final output of its evaluation.

Even no-op methods like `println` have a return type.

`:q` to exit REPL.

## Building Blocks

Over the course, will be building a File Searcher app, requirements:

* Find all files that match a given filter at a given location
  * Only matchfiles
  * Use current location if given location is ommitted
  * Search sub-folders from given location
* Further filter files using a content filter
* Return number of matches found in file
* Allow filters to be regular expressions
* Write results to a given file

### Simple Build Tool (sbt)

* Built-in defaults
  * compile
  * test
  * run

No configuration needed, given that default project structure is followed (based on maven):

![SBT Project Structure](images/sbt-structure.png "SBT Project Structure")

Can also run `sbt` interactively, just enter it into a terminal at project root. It will automatically compile any source it finds, even in project root (although not recommended to place source in root), [example](projects/hello/HelloSbt.scala).

`console` task opens a REPL with project classpath already set.

Can instantiate project class at REPL, for example:

```scala
new Main
res0: Main = Main@27778cce
```

It works with no parens, recommendation is to only use no parens when action has no side effect.
For example `sayHi` method does have side effect of writing to console, so it would be invoked with parens: `res0.sayHi()`.

More on [Scala style](http://docs.scala-lang.org/style/).

To exit sbt interactive mode, type `exit`.

To setup a proper project, create a [build.sbt](projects/filesearcher/build.sbt) file in project root.

Note that blank line acts as delimiter between settings.

`name` and `version` are the minimal settings needed to package project into a jar.

If don't scpecify `scalaVersion`, then it will use whatever version sbt was built against, better to specify.

Create a `project` directory, which will contain [build.properties](projects/filesearcher/build.properties) and [plugins.sbt](projects/filesearcher/project/plugins.sbt)

SBT uses [Apachy Ivy](http://ant.apache.org/ivy/ ) for dependency management. First value is group id, second is artifact id, and third is revision:

```scala
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")
```

After all the setup is in place, open `build.sbt` in IntelliJ (assuming Language Scala and SBT plugins are installed), it will create this project structure:

```
src
├── main
│   ├── java
│   ├── scala
│   └── scala-2.11
└── test
    ├── java
    ├── scala
    └── scala-2.11
```

Right-click on `src/main/scala` and select "Create new Worksheet", name it [Testbed.sc](projects/filesearcher/src/main/scala/Testbed.sc).

A worksheet provides REPL-like feedback within the IDE.

### ScalaTest

[Example](projects/filesearcher/src/test/scala/fileSearcher/FilterCheckerTests.scala)

Will use unit tests to codify the app requirements. Tests will be written in [ScalaTest](http://www.scalatest.org/) and run with [JUnit Test Runner](http://junit.org/junit4/). To use these, add `libraryDependencies` to [build.sbt](projects/filesearcher/build.sbt).

Use 4th argument to specify dependency is only needed for the "test" configuration.

```scala
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
```

**Choose Testing Style**

* xUnit -> FunSuite
* BDD -> FlatSpec (default)
* RSpec -> Flat Suite
* Acceptance -> FeatureSpec
* and [more](http://www.scalatest.org/user_guide/selecting_a_style)...  

To use FlatSpec, start by extending the `FlatSpec` *trait*. A trait only contains definition signatures, similar to Java interface.
Except a trait can also contain implementations, similar to Java abstract class.

```scala
import org.scalatest.FlatSpec

class FilterCheckerTests extends FlatSpec {
  ...
}
```

`import` pulls in external libraries into scope. Can also use `_` to wildcard and pull in entire library, for example `import org.scalatest._` but better to be specific.

Note that `==` checks for *object* equality, not *reference* equality:

```scala
assert(matchedFiles == List(matchingFile))
```

For reference quality, use `.eq` method.

To run the tests, first enter sbt in interactive mode, then run the test task:

```shell
$ sbt
> test
```

To run any task in watch mode (i.e. re-run on any file changes), prepend task with `~`, for example:

```shell
> ~test
```

To add a constructor parameter to class, follow class name with a parenthesis enclosed parameter list (notice less verbose than Java):

```scala
class FileObject(val name: String) {

}
```

All parameters and regular classes are private by default. Adding `val` keyword makes the parameter public.

For a private parameter, omit the `val` keyword, although it still is a value object.

```scala
class FilterChecker(filter: String) {

}
```

To filter a for loop for only certain matches, use a *for comprehension*:

```scala
def findMatchedFiles(fileObjects: List[FileObject]) = {
  for(fileObject <- fileObjects
    if(matches(fileObject.name)))
  // yield to caller, so list gets built up for caller with just the matches
  yield fileObject
}
```

### Expressive Clean Code

Can use *companion object* using `object` keyword to eliminate need for `new`, [example](projects/filesearcher/src/main/scala/fileSearcher/FilterChecker.scala).

`object` is a way to create a singleton, or static class that can have methods called directly on it, without using `new` keyword.

When an object is created with the same name and in the same file as another class, then its referred to as a *companion object* to the class.

If the companion object defines an apply method, which returns an instance of the class, then a new instance of the class can be invoked without use of the `new` keyword (scala magic!).

**Infix Notation**

Any method that takes only one parameter can be called without the dot or parenthesis, for example, instead of:

```scala
def matches(content: String) = content.contains(filter)
```

Use:

```scala
def matches(content: String) = content contains filter
```

Infix notation should only be used for methods that have no side effects.

### Checking the File System

Note that any code that is in a class and not part of a method definition, will run as part of construction, for example:

```scala
class Matcher(filter: String, rootLocation: String) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))
}
```

**Case Classes**

Classes with the keyword `case` in front of them, for example:

```scala
case class FileObject(file: File) extends IOObject
```

Constructor arguments for caes classes are always public, so `val` keyword not required.

`case` class comes with its own *companion object* built in, therefore, don't need `new` keyword when instantiating one.

Another benefit is that it builds its own `equals` implementation based on constructor arguments.

Case classes also allow object decomposition and pattern matching (to be covered in more detail later in the course).

**Pattern Matching**

Take object to be matched against (`rootIOObject` in example below), followed by `match` keyword and list of scenarios, or cases to behandled.

Cases are handled in the order in which they appear, falling to the next case until match is found.

First case below specifies a variable named `file`, followed by colon, then the expected type `FileObject`.

Cases can also contain *guard classes*, in the example below, used in `file` case to check if the captured files name matches the criteria.
If the case matches and guard class also matches, then logic on right hand side of the arrow is executed, in this case, returning a `List` with a single file object:

```scala
val matchedFiles = rootIOObject match {
  case file: FileObject if FilterChecker(filter) matches file.name => List(file)
  case directory: DirectoryObject => ???
  case _ => List()
}
```

If everything on the left fails to match, then the next case is evaluated.

Note that `???` throws a NotImplementedException.

If no cases match, then the default case is executed, which is annotated with `_`.
If no default is provided, then any case that doesn't match results in a match error.

### Mapping the Data
