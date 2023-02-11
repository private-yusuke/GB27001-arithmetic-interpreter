fun main() {
    val interpreter = Interpreter(
        tokenizer = Tokenizer(),
        parser = Parser(),
        evaluator = Evaluator()
    )

    while (true) {
        print("> ")
        val line = readlnOrNull() ?: break
        println(interpreter.eval(line))
    }
}