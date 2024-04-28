
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#map(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult map")
class LazyResult_map_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Result<Integer, Integer> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final Function<Integer, String> successMapper = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.map(successMapper, failureMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_map_when_needed() {
        // Given
        final Result<Integer, Integer> lazy = new LazyResult<>(() -> success(123));
        final Function<Integer, String> successMapper = s -> SUCCESS;
        final Function<Integer, String> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.map(successMapper, failureMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasSuccessSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final LazyResult<Integer, Integer> lazy = new LazyResult<>(() -> failure(123));
        final Function<Integer, String> successMapper = s -> fail("Should not happen");
        final Function<Integer, String> failureMapper = f -> FAILURE;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.map(successMapper, failureMapper);
        // Then
        assertThat(result).isNotInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }
}
