/**{% if false %}*/

package example;

import static example.Fragments.expensiveCalculation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leakyabstractions.result.api.Result;
import com.leakyabstractions.result.lazy.LazyConsumer;
import com.leakyabstractions.result.lazy.LazyResults;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Example")
class Example_Test {

/** {% elsif include.test == "should_skip_expensive_calculation" %} The calculation is skipped because the lazy result is not evaluated */
@Test
void shouldSkipExpensiveCalculation() {
  AtomicLong timesExecuted = new AtomicLong();
  // Given
  Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  Result<String, Exception> transformed = lazy.mapSuccess(Object::toString);
  // Then
  assertNotNull(transformed);
  assertEquals(0L, timesExecuted.get());
} // End{% endif %}{% if false %}

/** {% elsif include.test == "should_execute_expensive_calculation" %} The calculation is executed because the lazy result is evaluated */
@Test
void shouldExecuteExpensiveCalculation() {
  AtomicLong timesExecuted = new AtomicLong();
  // Given
  Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  Result<String, Exception> transformed = lazy.mapSuccess(Object::toString);
  boolean success = transformed.hasSuccess();
  // Then
  assertTrue(success);
  assertEquals(1L, timesExecuted.get());
} // End{% endif %}{% if false %}

/** {% elsif include.test == "should_handle_success_eagerly" %} The lazy result is evaluated and then the success consumer is executed */
@Test
void shouldHandleSuccessEagerly() {
  AtomicLong timesExecuted = new AtomicLong();
  AtomicLong consumerExecuted = new AtomicLong();
  Consumer<Long> consumer = x -> consumerExecuted.incrementAndGet();
  // Given
  Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  lazy.ifSuccess(consumer);
  // Then
  assertEquals(1L, timesExecuted.get());
  assertEquals(1L, consumerExecuted.get());
} // End{% endif %}{% if false %}

/** {% elsif include.test == "should_handle_failure_eagerly" %} The lazy result is evaluated but the failure consumer is not executed */
@Test
void shouldHandleFailureEagerly() {
  AtomicLong timesExecuted = new AtomicLong();
  AtomicLong consumerExecuted = new AtomicLong();
  Consumer<Exception> consumer = x -> consumerExecuted.incrementAndGet();
  // Given
  Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  lazy.ifFailure(consumer);
  // Then
  assertEquals(1L, timesExecuted.get());
  assertEquals(0L, consumerExecuted.get());
} // End{% endif %}{% if false %}

/** {% elsif include.test == "should_handle_success_lazily" %} The lazy consumer is not executed because the lazy result is not evaluated */
@Test
void shouldHandleSuccessLazily() {
  AtomicLong timesExecuted = new AtomicLong();
  AtomicLong consumerExecuted = new AtomicLong();
  Consumer<Long> consumer = LazyConsumer
      .of(x -> consumerExecuted.incrementAndGet());
  // Given
  Result<Long, Exception> lazy = LazyResults
      .ofSupplier(() -> expensiveCalculation(timesExecuted));
  // When
  lazy.ifSuccess(consumer);
  // Then
  assertEquals(0L, timesExecuted.get());
  assertEquals(0L, consumerExecuted.get());
} // End{% endif %}{% if false %}

}
// {% endif %}