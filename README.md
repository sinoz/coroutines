
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

## Additions

This fork also provides some syntactic sugary that'll help you with explicit typing:

```$xslt
  type Suspendable0[Y, R] = Coroutine._0[Y, R]
  type Suspendable1[A, Y, R] = Coroutine._1[A, Y, R]
  type Suspendable2[A, B, Y, R] = Coroutine._2[A, B, Y, R]
  type Suspendable3[A, B, C, Y, R] = Coroutine._3[A, B, C, Y, R]

  type SuspensionPoint[Y, R] = Coroutine.Instance[Y, R]

  implicit def toSuspensionPoint[Y, R](c: Any): SuspensionPoint[Y, R] = c.asInstanceOf[SuspensionPoint[Y, R]]

  implicit def toSuspendable0[Y, R](c: Any): Suspendable0[Y, R] = c.asInstanceOf[Suspendable0[Y, R]]
  implicit def toSuspendable1[A, Y, R](c: Any): Suspendable1[A, Y, R] = c.asInstanceOf[Suspendable1[A, Y, R]]
  implicit def toSuspendable2[A, B, Y, R](c: Any): Suspendable2[A, B, Y, R] = c.asInstanceOf[Suspendable2[A, B, Y, R]]
  implicit def toSuspendable3[A, B, C, Y, R](c: Any): Suspendable3[A, B, C, Y, R] = c.asInstanceOf[Suspendable3[A, B, C, Y, R]]
```

which also helps IntelliJ IDEA and other IDE's with proper error reporting as types will be known at compile-time.

```$xslt
  def main(args: Array[String]): Unit = {
    val action: SuspensionPoint[Int, Unit] = call(someLogic())

    println(action.resume)
  }

  def someLogic: Suspendable0[Int, Unit] =
    coroutine[Int, Unit] { () =>
      var i = 0
      while (i < 10) {
        yieldval(i)
        i += 1
      }

      println("Hello World")
    }
```

[![Join the chat at https://gitter.im/storm-enroute/coroutines](https://badges.gitter.im/storm-enroute/coroutines.svg)](https://gitter.im/storm-enroute/coroutines?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[Coroutines](http://storm-enroute.com/coroutines)
is a library-level extension for the Scala programming language
that introduces first-class coroutines.

Check out the [Scala Coroutines website](http://storm-enroute.com/coroutines) for more info!
