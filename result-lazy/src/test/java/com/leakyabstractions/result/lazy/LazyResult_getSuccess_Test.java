
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#getSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult getSuccess")
class LazyResult_getSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_success() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success(SUCCESS));
        // When
        final Optional<String> success = lazy.getSuccess();
        // Then
        assertThat(success).containsSame(SUCCESS);
    }

    @Test
    void should_return_empty() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final Optional<Integer> success = lazy.getSuccess();
        // Then
        assertThat(success).isEmpty();
    }
}
