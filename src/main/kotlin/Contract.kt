interface Contract {
    interface Tokenizer {
        fun tokenize(s: String): List<Token>
    }

    interface Parser {
        fun parse(tokens: List<Token>): Expression
    }

    interface Evaluator {
        fun evaluate(expression: Expression): Long
    }

    interface Interpreter {
        fun eval(s: String): Long?
    }

    sealed interface Token {
        data class Num(
            val value: Long,
        ) : Token

        object LeftParen : Token
        object RightParen : Token
        object Plus : Token
        object Minus : Token
        object Asterisk : Token
        object Slash : Token
        object Eof : Token
    }

    sealed interface Expression {
        @JvmInline
        value class Integer(
            val value: Long
        ) : Expression

        sealed interface BinaryOp : Expression {
            val lhs: Expression
            val rhs: Expression

            data class Addition(
                override val lhs: Expression,
                override val rhs: Expression,
            ) : BinaryOp

            data class Subtraction(
                override val lhs: Expression,
                override val rhs: Expression,
            ) : BinaryOp

            data class Multiplication(
                override val lhs: Expression,
                override val rhs: Expression,
            ) : BinaryOp

            data class Division(
                override val lhs: Expression,
                override val rhs: Expression,
            ) : BinaryOp
        }
    }
}