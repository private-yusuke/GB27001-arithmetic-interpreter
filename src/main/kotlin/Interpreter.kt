class Interpreter constructor(
    private val tokenizer: Tokenizer,
    private val parser: Parser,
    private val evaluator: Contract.Evaluator,
) : Contract.Interpreter {
    override fun eval(s: String): Long? {
        val tokens = tokenizer.tokenize(s)

        // If the first token is EOF, meaning that the input was
        // constructed only with spaces, then avoid evaluating the given code
        if (tokens.first() == Contract.Token.Eof) {
            return null
        }
        val parsed = parser.parse(tokens)

        return evaluator.evaluate(parsed)
    }

}