
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#getFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult getFailure")
class LazyResult_getFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_null() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success("SUCCESS"));
        // When
        final Integer failure = lazy.getFailure();
        // Then
        assertThat(failure).isNull();
    }

    @Test
    void should_return_failure() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        // String
        final String failure = lazy.getFailure();
        // Then
        assertThat(failure).isSameAs(FAILURE);
    }
}
