import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class ParserTest {
    @ParameterizedTest
    @ArgumentsSource(ParseValidArguments::class)
    fun parseValid(
        expected: Contract.Expression,
        actualInput: List<Contract.Token>,
    ) {
        val parser = Parser()
        parser.assertEquals(expected, actualInput)
    }

    private class ParseValidArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            Stream.of(
                // 2
                Arguments.arguments(
                    Contract.Expression.Integer(2),
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Eof,
                    )
                ),
                // 2 + 3
                Arguments.arguments(
                    Contract.Expression.BinaryOp.Addition(
                        Contract.Expression.Integer(2),
                        Contract.Expression.Integer(3),
                    ),
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Plus,
                        Contract.Token.Num(3),
                        Contract.Token.Eof,
                    )
                ),
                // 2 - 3
                Arguments.arguments(
                    Contract.Expression.BinaryOp.Subtraction(
                        Contract.Expression.Integer(2),
                        Contract.Expression.Integer(3),
                    ),
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Minus,
                        Contract.Token.Num(3),
                        Contract.Token.Eof,
                    )
                ),
                // 2 * 3
                Arguments.arguments(
                    Contract.Expression.BinaryOp.Multiplication(
                        Contract.Expression.Integer(2),
                        Contract.Expression.Integer(3),
                    ),
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Asterisk,
                        Contract.Token.Num(3),
                        Contract.Token.Eof,
                    )
                ),
                // 2 / 3
                Arguments.arguments(
                    Contract.Expression.BinaryOp.Division(
                        Contract.Expression.Integer(2),
                        Contract.Expression.Integer(3),
                    ),
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Slash,
                        Contract.Token.Num(3),
                        Contract.Token.Eof,
                    )
                ),
                // 2 + 3 * 4
                Arguments.arguments(
                    Contract.Expression.BinaryOp.Addition(
                        Contract.Expression.Integer(2),
                        Contract.Expression.BinaryOp.Multiplication(
                            Contract.Expression.Integer(3),
                            Contract.Expression.Integer(4),
                        )
                    ),
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Plus,
                        Contract.Token.Num(3),
                        Contract.Token.Asterisk,
                        Contract.Token.Num(4),
                        Contract.Token.Eof,
                    )
                ),
                // (2 + 3) * 4
                Arguments.arguments(
                    Contract.Expression.BinaryOp.Multiplication(
                        Contract.Expression.BinaryOp.Addition(
                            Contract.Expression.Integer(2),
                            Contract.Expression.Integer(3),
                        ),
                        Contract.Expression.Integer(4),
                    ),
                    listOf(
                        Contract.Token.LeftParen,
                        Contract.Token.Num(2),
                        Contract.Token.Plus,
                        Contract.Token.Num(3),
                        Contract.Token.RightParen,
                        Contract.Token.Asterisk,
                        Contract.Token.Num(4),
                        Contract.Token.Eof,
                    )
                ),
            )
    }

    @Test
    fun throwsExceptionWithInvalidTokenSeries() {
        val parser = Parser()

        parser.assertThrows<NoTokensLeftException>(
            listOf(
                Contract.Token.LeftParen,
                Contract.Token.Num(2),
            )
        )
        parser.assertThrows<IllegalStateException>(
            listOf(
                Contract.Token.LeftParen,
                Contract.Token.Num(2),
                Contract.Token.RightParen,
                Contract.Token.RightParen,
            )
        )
        parser.assertThrows<IllegalStateException>(
            listOf(
                Contract.Token.Num(2),
                Contract.Token.Num(3),
            )
        )
        parser.assertThrows<IllegalStateException>(
            listOf(
                Contract.Token.Plus,
            )
        )
    }
}

private fun Parser.assertEquals(
    expected: Contract.Expression,
    actualInput: List<Contract.Token>
) = assertEquals(expected, parse(actualInput))

private inline fun <reified T : Exception> Parser.assertThrows(
    actualInput: List<Contract.Token>
) = assertThrows<T> { parse(actualInput) }
