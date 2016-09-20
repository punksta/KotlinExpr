/**
 * Created by punksta on 14.09.16.
 */


fun variable(name: String) = Expr.Var(name)

fun const(c: Number) = Expr.Const(c)

//const(1) * const(2) == const(1).times(const(2))
infix operator fun Expr.times(expr: Expr): Expr = Expr.Mult(this, expr)

infix operator fun Expr.times(expr: Number): Expr = Expr.Mult(this, const(expr))
infix operator fun Expr.times(expr: String): Expr = Expr.Mult(this, Expr.Var(expr))

infix operator fun Expr.plus(expr: Expr): Expr = Expr.Sum(this, expr)
infix operator fun Expr.plus(expr: Number): Expr = Expr.Sum(this, const(expr))
infix operator fun Expr.plus(expr: String): Expr = Expr.Sum(this, Expr.Var(expr))

infix operator fun Expr.div(expr: Expr): Expr = Expr.Div(this, expr)
infix operator fun Expr.div(expr: Number): Expr = Expr.Div(this, const(expr))
infix operator fun Expr.div(expr: String): Expr = Expr.Div(this, Expr.Var(expr))


infix operator fun Expr.Const.plus(expr: Expr.Const): Expr.Const = when (this.c) {
    is Double -> const(this.c + expr.c.toDouble())
    is Int -> const(this.c + expr.c.toInt())
    is Long -> const(this.c + expr.c.toLong())
    is Float -> const(this.c + expr.c.toFloat())
    is Byte -> const(this.c + expr.c.toByte())
    else -> const(Float.NaN)
}

infix operator fun Expr.Const.times(expr: Expr.Const): Expr.Const = when (this.c) {
    is Double -> const(this.c * expr.c.toDouble())
    is Int -> const(this.c * expr.c.toInt())
    is Float -> const(this.c * expr.c.toFloat())
    is Byte -> const(this.c * expr.c.toByte())
    is Long -> const(this.c * expr.c.toLong())

    else -> const(Float.NaN)
}

infix operator fun Expr.Const.div(expr: Expr.Const): Expr.Const = when (this.c) {
    is Double -> const(this.c / expr.c.toDouble())
    is Int -> const(this.c / expr.c.toInt())
    is Float -> const(this.c / expr.c.toFloat())
    is Byte -> const(this.c / expr.c.toByte())
    is Long -> const(this.c / expr.c.toLong())
    else -> const(Float.NaN)
}

//where
infix fun Expr.where(pair: Pair<String, Expr>) = Expr.Let(pair.first, pair.second, this)

@JvmName("whereInt")
infix fun Expr.where(pair: Pair<String, Int>) = Expr.Let(pair.first, const(pair.second), this)

@JvmName("whereString")
infix fun Expr.where(pair: Pair<String, String>) = Expr.Let(pair.first, variable(pair.second), this)

//let and
infix fun Expr.and(pair: Pair<String, Int>) = Expr.Let(pair.first, const(pair.second), this)

@JvmName("andExr")
infix fun Expr.and(pair: Pair<String, Expr>) = Expr.Let(pair.first, pair.second, this)


//let реализуется через вспомогательный класс:
// ("s".let(1)).inExr(variable("s"))
class ExprBuilder(val name: String, val value: Expr) {
    infix fun inExr(expr: Expr): Expr
            = Expr.Let(name, value, expr)
}

infix fun String.let(expr: Expr) = ExprBuilder(this, expr)
infix fun String.let(constInt: Int) = ExprBuilder(this, const(constInt))


//if
class ifExprBuilder(val boolExp: BoolExp) : (Expr) -> elseExprBuilder {
    override fun invoke(p1: Expr): elseExprBuilder = ifTrue(p1)
    infix fun ifTrue(expr: Expr) = elseExprBuilder(boolExp, expr)
}

class elseExprBuilder(val boolExp: BoolExp, val ifTreue: Expr) {
    infix fun ifFalse(expr: Expr) = Expr.If(boolExp, ifTreue, expr)
    infix fun ifFalse(expr: () -> Expr) = Expr.If(boolExp, ifTreue, expr())

}

fun ifE(expr: Expr, key: BoolKey, expr2: Expr) = ifExprBuilder(BoolExp(expr, key, expr2))

fun ifE(expr: Expr, key: BoolKey, expr2: Expr, ifTrue: () -> Expr) = elseExprBuilder(BoolExp(expr, key, expr2), ifTrue.invoke())
