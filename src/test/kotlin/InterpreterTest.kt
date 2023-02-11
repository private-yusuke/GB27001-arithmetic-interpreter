import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {
    @ParameterizedTest
    @ArgumentsSource(EvaluationValidArguments::class)
    fun evaluationValid(
        expected: Long,
        actualInput: String,
    ) {
        val interpreter = Interpreter(
            tokenizer = Tokenizer(),
            parser = Parser(),
            evaluator = Evaluator()
        )

        assertEquals(expected, interpreter.eval(actualInput))
    }

    private class EvaluationValidArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            Stream.of(
                Arguments.arguments(2, "2"),
                Arguments.arguments(2, "(2)"),
                Arguments.arguments(2, "(((((2)))))"),

                Arguments.arguments(2, "1 + 1"),
                Arguments.arguments(14, "11 + 3"),
                Arguments.arguments(0, "1 - 1"),
                Arguments.arguments(8, "11 - 3"),
                Arguments.arguments(1, "1 * 1"),
                Arguments.arguments(33, "11 * 3"),
                Arguments.arguments(1, "1 / 1"),
                Arguments.arguments(3, "11 / 3"),

                Arguments.arguments(7, "1 + 2 * 3"),
                Arguments.arguments(9, "(1 + 2) * 3"),

                Arguments.arguments(4, "8 / 4 * 2"),
                Arguments.arguments(1, "8 / (4 * 2)"),
            )
    }

    @Test
    fun returnNullIfOnlySpaces() {
        val interpreter = Interpreter(
            tokenizer = Tokenizer(),
            parser = Parser(),
            evaluator = Evaluator()
        )

        interpreter.assertEquals(null, " ")
    }
}

private fun Interpreter.assertEquals(
    expected: Long?,
    actualInput: String,
) = assertEquals(expected, eval(actualInput))
