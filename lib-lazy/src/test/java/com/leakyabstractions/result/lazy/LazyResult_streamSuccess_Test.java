
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#streamSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult streamSuccess")
class LazyResult_streamSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success(SUCCESS));
        // When
        final Stream<String> stream = lazy.streamSuccess();
        // Then
        assertThat(stream).singleElement().isSameAs(SUCCESS);
    }

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final Stream<Integer> stream = lazy.streamSuccess();
        // Then
        assertThat(stream).isEmpty();
    }
}
