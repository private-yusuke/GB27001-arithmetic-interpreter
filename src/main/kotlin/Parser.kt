class Parser : Contract.Parser {
    private lateinit var tokens: List<Contract.Token>
    private var ind = 0

    override fun parse(tokens: List<Contract.Token>): Contract.Expression {
        this.tokens = tokens
        ind = 0

        val expr = parseExpr()

        when {
            this.tokens.size - 1 != ind ->
                throw IllegalStateException(
                    "All tokens must be consumed, but these are left: ${
                        this.tokens.subList(
                            ind,
                            this.tokens.size - 1
                        )
                    }"
                )

            this.tokens.getOrNull(ind) != Contract.Token.Eof ->
                throw IllegalStateException("Expected Eof, but got ${this.tokens.getOrNull(ind)}")

            else -> return expr
        }
    }

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

    private fun consume(token: Contract.Token): Boolean {
        val current = tokens.getOrNull(ind) ?: return false
        if (current == token) {
            ind++
            return true
        } else return false
    }

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

    private fun parseNumber(): Contract.Expression.Integer {
        val current = tokens[ind]
        if (current is Contract.Token.Num) {
            ind++
            return Contract.Expression.Integer(current.value)
        } else {
            throw IllegalStateException("Expected Num, but got $current")
        }
    }

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