/**
 * Created by punksta on 14.09.16.
 */


sealed class Expr {
    open class Const(val c: Int): Expr()
    object NAN : Const(0)

    class Var(val name : String) : Expr()

    class Sum(val left : Expr, val right: Expr) : Expr()
    class Mult(val left: Expr, val  right: Expr) : Expr()
    class Let(val name: String, val value: Expr, val expr: Expr) : Expr()

    override fun toString(): String = when(this) {
        is NAN -> "Not a number"
        is Const -> "$c"
        is Sum -> "($left + $right)"
        is Mult -> "($left * $right)"
        is Var -> name
        is Let -> "($expr, where $name = $value)"
    }
}


fun Expr.isNull() = if (this is Expr.Const) ((c == 0) and (this !is Expr.NAN)) else false
fun Expr.isNan() = (this is Expr.NAN)

