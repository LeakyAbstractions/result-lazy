
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#hasFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult hasFailure")
class LazyResult_hasFailure_Test {

    @Test
    void should_return_false() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success("SUCCESS"));
        // When
        final boolean hasFailure = lazy.hasFailure();
        // Then
        assertThat(hasFailure).isFalse();
    }

    @Test
    void should_return_true() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final boolean hasFailure = lazy.hasFailure();
        // Then
        assertThat(hasFailure).isTrue();
    }
}
