
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#getFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult getFailure")
class LazyResult_getFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_empty() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success("SUCCESS"));
        // When
        final Optional<Integer> failure = lazy.getFailure();
        // Then
        assertThat(failure).isEmpty();
    }

    @Test
    void should_return_failure() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        // String
        final Optional<String> failure = lazy.getFailure();
        // Then
        assertThat(failure).containsSame(FAILURE);
    }
}
