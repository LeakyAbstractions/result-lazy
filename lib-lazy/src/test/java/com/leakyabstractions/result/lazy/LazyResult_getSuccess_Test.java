
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

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
        final String success = lazy.getSuccess();
        // Then
        assertThat(success).isSameAs(SUCCESS);
    }

    @Test
    void should_return_null() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final Integer success = lazy.getSuccess();
        // Then
        assertThat(success).isNull();
    }
}
