
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#toString()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult toString")
class LazyResult_toString_Test {

    @Test
    void should_return_expected_string_when_not_supplied() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // Then
        assertThat(lazy).hasToString("LazyResult[Not supplied]");
    }

    @Test
    void should_return_expected_string_when_supplied() {
        // Given
        final LazyResult<String, Integer> lazy = new LazyResult<>(() -> success("OK"));
        // When
        lazy.getSupplied();
        // Then
        assertThat(lazy).hasToString("LazyResult[Success[OK]]");
    }
}
