/**
 * Created by punksta on 14.09.16.
 */


sealed class Expr {
    open class Const(val c: Number): Expr(), Comparable<Const> {
        override fun compareTo(other: Const): Int = when (c) {
            is Double -> c.compareTo(other.c.toDouble())
            is Float -> c.compareTo(other.c.toFloat())
            else -> c.toLong().compareTo(other.c.toLong())
        }
    }

    class Var(val name : String) : Expr()

    class Sum(val left : Expr, val right: Expr) : Expr()
    class Mult(val left: Expr, val  right: Expr) : Expr()
    class Div(val left: Expr, val  right: Expr) : Expr()
    class Let(val name: String, val value: Expr, val expr: Expr) : Expr()
    class If(val be: BoolExp, val ifTrue: Expr, val ifFalse: Expr) : Expr()

    override fun toString(): String = when(this) {
        is Const -> "$c"
        is Sum -> "($left + $right)"
        is Mult -> "($left * $right)"
        is Var -> name
        is Let -> "($expr, where $name = $value)"
        is Div -> "($left / $right)"
        is If -> "if (${be.expr1} ${be.key.name} ${be.expr2}) " +
                "then ($ifTrue) " +
                "else ($ifFalse)"
    }
}

sealed class Bool {
    object True : Bool()
    object False : Bool()
}

enum  class BoolKey {
    Less, Bigger, Equals
}

fun checkBoolExpr(key: BoolKey, e1: Expr.Const, e2: Expr.Const) = when(key) {
    BoolKey.Equals -> e1.c == e2.c
    BoolKey.Less -> e1 < e2
    BoolKey.Bigger -> e1 > e2
}

class BoolExp(val expr1: Expr, val key: BoolKey, val expr2: Expr)


fun Expr.isNull() = if (this is Expr.Const) (c == 0) else false



