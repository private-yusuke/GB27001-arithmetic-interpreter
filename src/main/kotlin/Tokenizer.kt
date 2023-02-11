class Tokenizer : Contract.Tokenizer {
    override fun tokenize(s: String): List<Contract.Token> {
        var ind = 0
        val res: MutableList<Contract.Token> = mutableListOf()

        while (ind < s.length) {
            val c = s[ind]

            when {
                c == ' ' -> ind++
                c in "+-*/()" -> {
                    res += c.toSymbol()
                    ind++
                }

                c.isDigit() -> {
                    val lastIndex = s.findNextIntLastIndex(ind)
                    val value = s.substring(ind..lastIndex).toLong()
                    res += Contract.Token.Num(value)
                    ind = lastIndex + 1
                }

                else -> throw IllegalStateException("Unknown character: $c")
            }
        }
        res += Contract.Token.Eof

        return res
    }

    private fun Char.toSymbol() = when (this) {
        '+' -> Contract.Token.Plus
        '-' -> Contract.Token.Minus
        '*' -> Contract.Token.Asterisk
        '/' -> Contract.Token.Slash
        '(' -> Contract.Token.LeftParen
        ')' -> Contract.Token.RightParen
        else -> throw IllegalStateException("Expected symbols, but got $this")
    }

    private fun String.findNextIntLastIndex(
        startIndex: Int
    ): Int {
        for (ind in startIndex until length) {
            val c = this[ind]
            if (!c.isDigit()) {
                return ind - 1
            }
        }

        return length - 1
    }
}