
![Coroutines](/coroutines-128-xmas.png)

# Scala Coroutines

This fork adds experimental support for Scala 2.12. To make use of this, clone this repository and hit the following in terminal or command line:

```$xslt
sbt publishLocal
```

Which will publish the `coroutines` and `coroutines-common` libraries to your local ivy folder in your user home. Once this task is done, you can use this directly from your SBT project:

```$xslt
libraryDependencies ++= Seq(
  "com.storm-enroute" %% "coroutines" % "1.0"
)
``` 

[![Join the chat at https://gitter.im/storm-enroute/coroutines](https://badges.gitter.im/storm-enroute/coroutines.svg)](https://gitter.im/storm-enroute/coroutines?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[Coroutines](http://storm-enroute.com/coroutines)
is a library-level extension for the Scala programming language
that introduces first-class coroutines.

Check out the [Scala Coroutines website](http://storm-enroute.com/coroutines) for more info!