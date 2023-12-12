/*{% if false %}*/

package example;

import static com.leakyabstractions.result.core.Results.success;

import com.leakyabstractions.result.api.Result;
import com.leakyabstractions.result.lazy.LazyResults;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

class Fragments {

  static void creation() {

/* {% elsif include.fragment == "creation" %} Create new lazy result */
Supplier<Result<Integer, String>> supplier = () -> success(123);
Result<Integer, String> lazy = LazyResults.ofSupplier(supplier); // Just like that!{% endif %}{% if false %}

  }

static
/* {% elsif include.fragment == "expensive_calculation" %} Represents the operation we could omit */
Result<Long, Exception> expensiveCalculation(AtomicLong timesExecuted) {
  long counter = timesExecuted.incrementAndGet();
  return success(counter);
} // In this example, we just increment and return current count{% endif %}{% if false %}

}
// {% endif %}