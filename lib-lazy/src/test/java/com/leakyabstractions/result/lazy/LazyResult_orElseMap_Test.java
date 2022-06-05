
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
 * Tests for {@link LazyResult#orElseMap(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult orElseMap")
class LazyResult_orElseMap_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_use_failure_mapper() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        final Function<String, Integer> mapper = f -> 321;
        // When
        final Integer value = lazy.orElseMap(mapper);
        // Then
        assertThat(value).isEqualTo(321);
    }

    @Test
    void should_ignore_failure_mapper() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Function<Integer, String> mapper = f -> fail("Should not happen");
        // When
        final String value = lazy.orElseMap(mapper);
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }
}
