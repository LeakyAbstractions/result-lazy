
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#optional()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult optional")
class LazyResult_optional_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_non_empty_optional() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success(SUCCESS));
        // When
        final Optional<String> optional = lazy.optional();
        // Then
        assertThat(optional).containsSame(SUCCESS);
    }

    @Test
    void should_return_empty_optional() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final Optional<Integer> optional = lazy.optional();
        // Then
        assertThat(optional).isEmpty();
    }
}
