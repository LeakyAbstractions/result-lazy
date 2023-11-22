/**{% if false %}*/

package example;

import static example.Fragments.expensiveCalculation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leakyabstractions.result.api.Result;
import com.leakyabstractions.result.lazy.LazyResults;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Example")
class Example_Test {

/** {% elsif include.test == "should_not_execute_expensive_action" %} The operation is omitted because the lazy result is not fully evaluated */
@Test
void should_not_execute_expensive_action() {
  final AtomicLong timesExecuted = new AtomicLong();
  // Given
  final Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  final Result<String, Exception> transformed = lazy.mapSuccess(Object::toString);
  // Then
  assertNotNull(transformed);
  assertEquals(0L, timesExecuted.get());
} // End{% endif %}{% if false %}

/** {% elsif include.test == "should_execute_expensive_action" %} The operation is executed because the lazy result is evaluated */
@Test
void should_execute_expensive_action() {
  final AtomicLong timesExecuted = new AtomicLong();
  // Given
  final Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  final Result<String, Exception> transformed = lazy.mapSuccess(Object::toString);
  final boolean success = transformed.hasSuccess();
  // Then
  assertTrue(success);
  assertEquals(1L, timesExecuted.get());
} // End{% endif %}{% if false %}

}
// {% endif %}