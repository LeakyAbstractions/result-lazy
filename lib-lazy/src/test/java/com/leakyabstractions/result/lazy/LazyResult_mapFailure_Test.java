
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#mapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult mapFailure")
class LazyResult_mapFailure_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String ANOTHER = "ANOTHER";

    @Test
    void should_be_lazy() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final Function<String, Integer> mapper = f -> fail("Should not happen");
        // When
        final Result<String, Integer> result = lazy.mapFailure(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_map_when_needed() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final Function<String, String> mapper = f -> ANOTHER;
        // When
        final Result<String, String> result = lazy.mapFailure(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasFailureSameAs(ANOTHER);
    }

    @Test
    void should_ignore_failure_mapping() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Function<String, String> mapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.mapFailure(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasSuccessSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final LazyResult<String, Integer> lazy = new LazyResult<>(() -> failure(123));
        final Function<Integer, String> mapper = f -> FAILURE;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.mapFailure(mapper);
        // Then
        assertThat(result).isNotInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }
}
