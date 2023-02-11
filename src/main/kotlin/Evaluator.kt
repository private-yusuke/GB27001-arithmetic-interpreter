class Evaluator : Contract.Evaluator {
    override fun evaluate(expression: Contract.Expression): Long =
        expression.eval()

    private fun Contract.Expression.eval(): Long =
        when (this) {
            is Contract.Expression.Integer ->
                this.value

            is Contract.Expression.BinaryOp.Addition ->
                this.lhs.eval() + this.rhs.eval()

            is Contract.Expression.BinaryOp.Subtraction ->
                this.lhs.eval() - this.rhs.eval()

            is Contract.Expression.BinaryOp.Multiplication ->
                this.lhs.eval() * this.rhs.eval()

            is Contract.Expression.BinaryOp.Division ->
                when (val it = this.rhs.eval()) {
                    0L -> throw ZeroDivisionException
                    else -> this.lhs.eval() / it
                }
        }
}

object ZeroDivisionException : Exception()