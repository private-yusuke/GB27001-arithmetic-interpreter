class Parser : Contract.Parser {
    override fun parse(tokens: List<Contract.Token>): Contract.Expression =
        with(
            ParsingContext(
                ind = 0,
                tokens = tokens
            )
        ) {
            parseWithContext()
        }


    context(ParsingContext)
    private fun parseWithContext(): Contract.Expression {
        val expr = parseExpr()

        when {
            tokens.size - 1 != ind ->
                throw IllegalStateException(
                    "All tokens must be consumed, but these are left: ${
                        tokens.subList(
                            ind,
                            tokens.size - 1
                        )
                    }"
                )

            tokens.getOrNull(ind) != Contract.Token.Eof ->
                throw IllegalStateException("Expected Eof, but got ${tokens.getOrNull(ind)}")

            else -> return expr
        }
    }

    context(ParsingContext)
    private fun expect(token: Contract.Token) {
        if (tokens.size <= ind) {
            throw NoTokensLeftException("Expected $token, but no tokens left")
        }
        val current = tokens[ind]
        if (current != token) {
            throw IllegalStateException("Expected $token, but got $current")
        }
        ind++
    }

    context(ParsingContext)
    private fun consume(token: Contract.Token): Boolean {
        val current = tokens.getOrNull(ind) ?: return false
        if (current == token) {
            ind++
            return true
        } else return false
    }

    context(ParsingContext)
    private fun parseMul(): Contract.Expression {
        var expr = parsePrimary()

        while (true) {
            expr = when {
                consume(Contract.Token.Asterisk) ->
                    Contract.Expression.BinaryOp.Multiplication(expr, parsePrimary())

                consume(Contract.Token.Slash) ->
                    Contract.Expression.BinaryOp.Division(expr, parsePrimary())

                else -> return expr
            }
        }
    }

    context(ParsingContext)
    private fun parseExpr(): Contract.Expression {
        var expr = parseMul()

        while (true) {
            expr = when {
                consume(Contract.Token.Plus) ->
                    Contract.Expression.BinaryOp.Addition(expr, parseMul())

                consume(Contract.Token.Minus) ->
                    Contract.Expression.BinaryOp.Subtraction(expr, parseMul())

                else -> return expr
            }
        }
    }

    context(ParsingContext)
    private fun parseNumber(): Contract.Expression.Integer {
        val current = tokens[ind]
        if (current is Contract.Token.Num) {
            ind++
            return Contract.Expression.Integer(current.value)
        } else {
            throw IllegalStateException("Expected Num, but got $current")
        }
    }

    context(ParsingContext)
    private fun parsePrimary(): Contract.Expression {
        if (consume(Contract.Token.LeftParen)) {
            val expr = parseExpr()
            expect(Contract.Token.RightParen)
            return expr
        }

        return parseNumber()
    }
}

class NoTokensLeftException(override val message: String?) : Exception()

private data class ParsingContext(
    var ind: Int,
    val tokens: List<Contract.Token>,
)
