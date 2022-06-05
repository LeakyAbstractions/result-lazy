
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#hasSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult hasSuccess")
class LazyResult_hasSuccess_Test {

    @Test
    void should_return_true() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success("SUCCESS"));
        // When
        final boolean hasSuccess = lazy.hasSuccess();
        // Then
        assertThat(hasSuccess).isTrue();
    }

    @Test
    void should_return_false() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final boolean hasSuccess = lazy.hasSuccess();
        // Then
        assertThat(hasSuccess).isFalse();
    }
}
