/**
 * Created by punksta on 15.09.16.
 */

fun solve(p1: Expr, context: Map<String, Expr?  >? = null) : Expr.Const = when (p1) {
    is Expr.Const -> p1
    is Expr.Mult -> when {
        p1.left.isNull() or p1.right.isNull()-> const(0)
        p1.left.isNan() or p1.right.isNan()-> Expr.NAN
        else -> const(solve(p1.left, context).c * solve(p1.right, context).c)
    }
    is Expr.Sum -> when  {
        (p1.left.isNan() or p1.right.isNan())-> Expr.NAN
        else -> const(solve(p1.left, context).c + solve(p1.right, context).c)
    }
    is Expr.Var -> context?.get(p1.name).let {
        if (it == null) {
            throw IllegalArgumentException("undefined var ${p1.name}")
        } else {
            solve(it, context.orEmpty().filter { it.key != p1.name })
        }
    }
    is Expr.Let -> (context.orEmpty() + Pair(p1.name, p1.value)).let {
        solve(p1.expr, it)
    }
}