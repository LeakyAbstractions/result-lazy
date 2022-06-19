
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#orElse(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult orElse")
class LazyResult_orElse_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_other() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final Integer value = lazy.orElse(123);
        // Then
        assertThat(value).isEqualTo(123);
    }

    @Test
    void should_ignore_other() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success(SUCCESS));
        // When
        final String value = lazy.orElse("ANOTHER");
        // Then
        assertThat(value).isSameAs(SUCCESS);
    }
}
