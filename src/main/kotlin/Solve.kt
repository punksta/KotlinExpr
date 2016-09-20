/**
 * Created by punksta on 15.09.16.
 */

sealed class Result {
    class Error(val message: String, val expr: Expr) : Result()
    class Some(val const: Expr.Const) : Result()
    object Nan : Result()
}

fun some(expr: Expr.Const) = Result.Some(expr)
fun error(message: String, expr: Expr) = Result.Error(message, expr)
fun nan() = Result.Nan


fun solve(p1: Expr, context: Map<String, Expr?>? = null) : Result = when(p1) {
    is Expr.Const -> when (p1.c) {
        is Double -> if (p1.c.isNaN()) nan() else some(p1)
        is Float -> if (p1.c.isNaN()) nan() else some(p1)
        else -> some(p1)
    }
    is Expr.Let -> solve(p1.expr, context.orEmpty() + (p1.name to p1.value))
    is Expr.Var -> context?.get(p1.name).let {
        if (it == null)
            error("undefined var ${p1.name}", p1)
        else
            solve(it, context)
    }
    is Expr.Div -> solve(p1.left, context).applyIfSome { r1 ->
            solve(p1.right, context).applyIfSome { r2 ->
                if (r2.const.isNull())
                        nan()
                else
                    some(r1.const / r2.const)
            }
    }
    is Expr.Sum -> solve(p1.left, context).applyIfSome { r1 ->
            solve(p1.right, context).applyIfSome { r2 ->
                    some(r1.const + r2.const)
            }
    }
    is Expr.Mult -> solve(p1.left, context).applyIfSome { r1->
            solve(p1.right, context).applyIfSome { r2 ->
                    some(r1.const * r2.const)
            }
    }
    is Expr.If -> solve(p1.be.expr1).applyIfSome { r1 ->
        solve(p1.be.expr2).applyIfSome { r2 ->
            (if (checkBoolExpr(p1.be.key, r1.const, r2.const))
                p1.ifTrue
            else p1.ifFalse).let { e ->
                solve(e, context)
            }
        }
    }
}

private fun Result.applyIfSome(apply: (Result.Some) -> Result) : Result
        = if (this is Result.Some) apply(this) else this
