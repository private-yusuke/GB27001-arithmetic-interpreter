import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

class EvaluatorTest {
    @ParameterizedTest
    @ArgumentsSource(EvaluationValidArguments::class)
    fun evaluationValid(
        expected: Long,
        actualInput: Contract.Expression,
    ) {
        val evaluator = Evaluator()
        evaluator.assertEquals(expected, actualInput)
    }

    private class EvaluationValidArguments : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
            Stream.of(
                Arguments.arguments(
                    2,
                    Contract.Expression.Integer(2)
                ),
                Arguments.arguments(
                    4,
                    Contract.Expression.BinaryOp.Addition(
                        Contract.Expression.Integer(1),
                        Contract.Expression.Integer(3),
                    )
                ),
                Arguments.arguments(
                    -5,
                    Contract.Expression.BinaryOp.Multiplication(
                        Contract.Expression.BinaryOp.Subtraction(
                            Contract.Expression.Integer(3),
                            Contract.Expression.Integer(4),
                        ),
                        Contract.Expression.Integer(5),
                    )
                ),
                Arguments.arguments(
                    0,
                    Contract.Expression.BinaryOp.Division(
                        Contract.Expression.Integer(2),
                        Contract.Expression.Integer(3),
                    )
                ),
            )
    }

    @Test
    fun throwsZeroDivisionException() {
        val evaluator = Evaluator()
        evaluator.assertThrows<ZeroDivisionException>(
            Contract.Expression.BinaryOp.Division(
                Contract.Expression.Integer(1),
                Contract.Expression.Integer(0),
            )
        )
    }
}

private fun Evaluator.assertEquals(
    expected: Long,
    actualInput: Contract.Expression,
) = assertEquals(expected, evaluate(actualInput))

private inline fun <reified T : Exception> Evaluator.assertThrows(
    actualInput: Contract.Expression,
) = assertThrows<T> { evaluate(actualInput) }
