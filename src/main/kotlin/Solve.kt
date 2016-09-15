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
    is Expr.Const -> some(p1)

    is Expr.Let -> solve(p1.expr, context.orEmpty() + Pair(p1.name, p1.value))

    is Expr.Var -> context?.get(p1.name).let {
        if (it == null)
            error("undefined var ${p1.name}", p1)
        else
            solve(it, context)
    }

    is Expr.Div -> solve(p1.left, context).let { r1 ->
        if (r1 is Result.Some)
            solve(p1.right, context).let { r2 ->
                if (r2 is Result.Some)
                    if (r2.const.isNull()) nan() else some(const(r1.const.c / r2.const.c))
                else r2
            }
        else r1
    }

    is Expr.Sum -> solve(p1.left, context).let { r1 ->
        if (r1 is Result.Some)
            solve(p1.right, context).let { r2 ->
                if (r2 is Result.Some)
                    some(const(r1.const.c + r2.const.c))
                else
                    r2
            }
        else
            r1
    }
    is Expr.Mult -> with(solve(p1.left, context)) {
        if (this is Result.Some)
            solve(p1.right, context).let { r2 ->
                if (r2 is Result.Some)
                    some(const(this.const.c*r2.const.c))
                else
                    r2
            }
        else
            this
    }
}