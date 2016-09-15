/**
 * Created by punksta on 14.09.16.
 */

fun testSolve(expr: Expr, shouldEquals: Expr.Const) {
    print(expr.toString())
    val c = solve(expr)
    println(" = $c, correct: ${shouldEquals.c == c.c}")
}

fun main(args: Array<String>) {
    val helloHabr = variable("hello") * variable("habr") * 3  where ("hello" to 1) and ("habr" to 2)
    testSolve(helloHabr, const(1*2*3))

    val e = (const(1) + const(2)) * const(3)
    testSolve(e, const((1 + 2) *3))

    val e2 = "x".let(10) inExr ("y".let(100) inExr (variable("x") + variable("y")))
    testSolve(e2, const(110))

    val e3 = (variable("x") * variable("x") * 2) where ("x" to 2)

    testSolve(e3, const(2*2*2))

    val e4 = "x" let (1) inExr (variable("x") + (variable("x") where ("x" to 2)))

    testSolve(e4, const(3))

    val e5 = "x" let (0) inExr (variable("x") * 1000 + variable("x"))
    testSolve(e5, const(0))
}