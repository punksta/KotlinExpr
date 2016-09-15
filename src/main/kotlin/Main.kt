/**
 * Created by punksta on 14.09.16.
 */

fun logSolve(expr: Expr) {
    println(expr)
    val c = solve(expr)
    when(c) {
        is Result.Error -> println("  error: ${c.message} inside ${c.expr}")
        is Result.Some -> println("   ${c.const}")
        is Result.Some -> println("   ${c}")
    }
}

fun main(args: Array<String>) {
    listOf<Expr>(
            variable("hello") * variable("habr") * 3  where ("hello" to 1) and ("habr" to 2),
            (const(1) + const(2)) * const(3),
            "x".let(10) inExr ("y".let(100) inExr (variable("x") + variable("y"))),
            (variable("x") * variable("x") * 2) where ("x" to 2),
            "x" let (1) inExr (variable("x") + (variable("x") where ("x" to 2))),
            "x" let (0) inExr (variable("x") * 1000 + variable("x")),
            (variable("x") * const(2) / 3)
    ).forEach { logSolve(it) }

}