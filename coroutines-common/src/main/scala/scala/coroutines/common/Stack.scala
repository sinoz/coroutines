package scala.coroutines.common



import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context



object Stack {
  def push[T](stack: Array[T], cd: Any): Unit = macro pushMacro[T]

  def pushMacro[T: c.WeakTypeTag](c: Context)(stack: c.Tree, cd: c.Tree): c.Tree = {
    import c.universe._

    val q"""$path.${name: TermName}""" = stack
    val stackptr = TermName(s"${name}ptr")
    val tpe = implicitly[WeakTypeTag[T]]
    q"""
    if ($stackptr >= $stack.length) {
      val nstack = new Array[$tpe]($stack.length * 2)
      java.lang.System.arraycopy($stack, 0, nstack, 0, $stack.length)
      $stack = nstack
    }
    $stack($stackptr) = $cd
    $stackptr += 1
    """
  }

  def pop[T](stack: Array[T]): T = macro popMacro[T]

  def popMacro[T: c.WeakTypeTag](c: Context)(stack: c.Tree): c.Tree = {
    import c.universe._

    val q"""$path.${name: TermName}""" = stack
    val stackptr = TermName(s"${name}ptr")
    val tpe = implicitly[WeakTypeTag[T]]
    val valnme = TermName(c.freshName())
    q"""
    $stackptr -= 1
    val $valnme = $stack($stackptr)
    $stack($stackptr) = null.asInstanceOf[$tpe]
    $valnme
    """
  }
}
