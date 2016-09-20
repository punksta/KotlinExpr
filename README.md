# KotlinExpr
Arithmetic operations language as kotlin dsl

```
fun main(args: Array<String>) {
                 listOf<Expr>(
                         variable("hello") * variable("habr") * 3  where ("hello" to 1) and ("habr" to 2),

                         (const(1) + const(2)) * const(3.5F),

                         "x".let(10) inExr ("y".let(100) inExr (variable("x") + variable("y"))),

                         (variable("x") * variable("x") * 2) where ("x" to 2),

                         "x" let (1) inExr (variable("x") + (variable("x") where ("x" to 2))),

                         "x" let (0) inExr (variable("x") * 1000 + variable("x")),

                         (variable("x") * const(2F) / 3F),

                         ifE(const(1), BoolKey.Less, const(2)) {
                             variable("helloHabr")
                         } ifFalse {
                             const(1) * const(123F)
                         } where ("helloHabr" to 2016)
             }
```


