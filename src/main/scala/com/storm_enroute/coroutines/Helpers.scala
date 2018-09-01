package com.storm_enroute.coroutines

object Helpers {
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
}
