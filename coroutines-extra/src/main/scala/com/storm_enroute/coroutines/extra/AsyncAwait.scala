package com.storm_enroute.coroutines.extra

import com.storm_enroute.coroutines._
import scala.annotation.unchecked.uncheckedVariance
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import scala.util.{ Success, Failure }



object AsyncAwait {
  /** Await the result of a future.
   *
   *  When called inside an `async` body, this function will block until its
   *  associated future completes.
   *
   *  @return A coroutine that yields a tuple. `async` will assign this tuple's
   *          second element to hold the completed result of the `Future` passed
   *          into the coroutine. The coroutine will directly return the
   *          result of the future.
   */
  def await[R]: Future[R] ~~> (Future[R], R) =
    coroutine { (awaitedFuture: Future[R]) =>
      yieldval(awaitedFuture)
      var result: R = null.asInstanceOf[R]
      awaitedFuture.value match {
        case Some(Success(x)) => result = x
        case Some(Failure(error)) => throw error
        case None => sys.error("Future was not completed")
      }
      result
    }

  /** Calls `body`, blocking on any calls to `await`.
   *
   *  @param body  A coroutine to be invoked.
   *  @return      A `Future` wrapping the result of the coroutine. The future fails
   *               if `body` throws an exception or one of the `await`s takes a failed
   *               future.
   */
  def asyncCall[Y, R](body: ~~~>[Future[Y], R]): Future[R] = {
    val c = call(body())
    val p = Promise[R]
    def loop() {
      if (!c.resume) {
        c.tryResult match {
          case Success(result) => p.success(result)
          case Failure(exception) => p.failure(exception)
        }
      } else {
        val awaitedFuture = c.value
        if (awaitedFuture.isCompleted) {
          loop()
        } else {
          awaitedFuture onComplete {
            case _ => loop()
          }
        }
      }
    }
    Future { loop() }
    p.future
  }

  /** Wraps `body` inside a coroutine and asynchronously invokes it using `asyncMacro`.
   *
   *  @param body  The block of code to wrap inside an asynchronous coroutine.
   *  @return      A `Future` wrapping the result of `body`.
   */
  def async[Y, R](body: =>R): Future[R] = macro asyncMacro[Y, R]

  /** Implements `async`.
   *
   *  Wraps `body` inside a coroutine and calls `asyncCall`.
   *
   *  @param body  The function to be wrapped in a coroutine.
   *  @return      A tree that contains an invocation of `asyncCall` on a coroutine
   *               with `body` as its body.
   */
  def asyncMacro[Y, R](c: Context)(body: c.Tree): c.Tree = {
    import c.universe._

    /** Ensures that no values are yielded inside the async block.
     *
     *  It is similar to and shares functionality with
     *  [[com.storm_enroute.coroutines.AstCanonicalization.NestedContextValidator]].
     *
     */
    class NoYieldsValidator extends Traverser {
      // return type is the lub of the function return type and yield argument types
      def isCoroutinesPkg(q: Tree) = q match {
        case q"com.storm_enroute.coroutines.`package`" => true
        case q"coroutines.this.`package`" => true
        case t => false
      }

      override def traverse(tree: Tree): Unit = tree match {
        case q"$qual.yieldval[$_]($_)" if isCoroutinesPkg(qual) =>
          c.abort(tree.pos,
            "The yieldval statement only be invoked directly inside the coroutine. " +
            "Nested classes, functions or for-comprehensions, should either use the " +
            "call statement or declare another coroutine.")
        case q"$qual.yieldto[$_]($_)" if isCoroutinesPkg(qual) =>
          c.abort(tree.pos,
            "The yieldto statement only be invoked directly inside the coroutine. " +
            "Nested classes, functions or for-comprehensions, should either use the " +
            "call statement or declare another coroutine.")
        case q"$qual.call($co.apply(..$args))" if isCoroutinesPkg(qual) =>
          // no need to check further, the call macro will validate the coroutine type
        case _ =>
          super.traverse(tree)
      }
    }

    new NoYieldsValidator().traverse(body)

    q"""
       val c = coroutine { () =>
         $body
       }
       _root_.com.storm_enroute.coroutines.extra.AsyncAwait.asyncCall(c)
     """
  }
}
