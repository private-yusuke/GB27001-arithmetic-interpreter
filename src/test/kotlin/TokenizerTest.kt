import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

class TokenizerTest {
    @ParameterizedTest
    @ArgumentsSource(TokenizeValidArguments::class)
    fun evaluationValid(
        expected: List<Contract.Token>,
        actualInput: String,
    ) {
        val tokenizer = Tokenizer()
        tokenizer.assertEquals(expected, actualInput)
    }

    private class TokenizeValidArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            Stream.of(
                Arguments.arguments(
                    listOf(Contract.Token.Eof),
                    " ",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Eof
                    ),
                    "2",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.LeftParen,
                        Contract.Token.Num(2),
                        Contract.Token.RightParen,
                        Contract.Token.Eof
                    ),
                    "(2)",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Plus,
                        Contract.Token.Num(3),
                        Contract.Token.Eof
                    ),
                    "2 + 3",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Minus,
                        Contract.Token.Num(3),
                        Contract.Token.Eof
                    ),
                    "2 - 3",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Asterisk,
                        Contract.Token.Num(3),
                        Contract.Token.Eof
                    ),
                    "2 * 3",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.Num(2),
                        Contract.Token.Slash,
                        Contract.Token.Num(3),
                        Contract.Token.Eof
                    ),
                    "2 / 3",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.Num(1),
                        Contract.Token.Plus,
                        Contract.Token.Num(2),
                        Contract.Token.Asterisk,
                        Contract.Token.Num(3),
                        Contract.Token.Eof
                    ),
                    "1 + 2 * 3",
                ),
                Arguments.arguments(
                    listOf(
                        Contract.Token.LeftParen,
                        Contract.Token.Num(1),
                        Contract.Token.Plus,
                        Contract.Token.Num(2),
                        Contract.Token.RightParen,
                        Contract.Token.Asterisk,
                        Contract.Token.Num(3),
                        Contract.Token.Eof
                    ),
                    "(1 + 2) * 3",
                ),
            )
    }

    @Test
    fun throwsExceptionWithUnknownCharacter() {
        val tokenizer = Tokenizer()

        tokenizer.assertThrows<IllegalStateException>("foo")
    }
}

private fun Tokenizer.assertEquals(
    expected: List<Contract.Token>,
    actualInput: String,
) = assertEquals(expected, tokenize(actualInput))

private inline fun <reified T : Exception> Tokenizer.assertThrows(
    actualInput: String,
) = assertThrows<T> { tokenize(actualInput) }
