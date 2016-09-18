/**
 * Created by punksta on 14.09.16.
 */


sealed class Expr {
    open class Const(val c: Number): Expr()

    class Var(val name : String) : Expr()

    class Sum(val left : Expr, val right: Expr) : Expr()
    class Mult(val left: Expr, val  right: Expr) : Expr()
    class Div(val left: Expr, val  right: Expr) : Expr()
    class Let(val name: String, val value: Expr, val expr: Expr) : Expr()


    override fun toString(): String = when(this) {
        is Const -> "$c"
        is Sum -> "($left + $right)"
        is Mult -> "($left * $right)"
        is Var -> name
        is Let -> "($expr, where $name = $value)"
        is Div -> "($left / $right)"
    }
}

fun Expr.isNull() = if (this is Expr.Const) (c == 0) else false

