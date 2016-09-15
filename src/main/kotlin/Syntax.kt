/**
 * Created by punksta on 14.09.16.
 */


fun variable(name: String) = Expr.Var(name)
fun const(c : Int) = Expr.Const(c)

//const(1) * const(2) == const(1).times(const(2))
infix operator fun Expr.times(expr: Expr): Expr = Expr.Mult(this, expr)
infix operator fun Expr.times(expr: Int): Expr = Expr.Mult(this, const(expr))
infix operator fun Expr.times(expr: String) : Expr = Expr.Mult(this, Expr.Var(expr))

//аналогично для плюс
infix operator fun Expr.plus(expr: Expr): Expr = Expr.Sum(this, expr)
infix operator fun Expr.plus(expr: Int): Expr = Expr.Sum(this, const(expr))
infix operator fun Expr.plus(expr: String) : Expr = Expr.Sum(this, Expr.Var(expr))


//where
infix fun Expr.where(pair: Pair<String, Expr>) = Expr.Let(pair.first, pair.second, this)
@JvmName("whereInt")
infix fun Expr.where(pair: Pair<String, Int>) = Expr.Let(pair.first, const(pair.second), this)
@JvmName("whereString")
infix fun Expr.where(pair: Pair<String, String>) = Expr.Let(pair.first, variable(pair.second), this)


//let реализуется через вспомогательный класс:
// ("s".let(1)).inExr(variable("s"))
class ExprBuilder(val name: String, val value: Expr) {
    infix fun inExr(expr: Expr): Expr
            = Expr.Let(name, value, expr)
}

infix fun String.let(expr: Expr) = ExprBuilder(this, expr)
infix fun String.let(constInt: Int) = ExprBuilder(this, const(constInt))


//let and
infix fun Expr.and(pair: Pair<String, Int>) = Expr.Let(pair.first, const(pair.second), this)

@JvmName("andExr")
infix fun Expr.and(pair: Pair<String, Expr>) = Expr.Let(pair.first, pair.second, this)
