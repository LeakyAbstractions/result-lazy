
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#filter(Predicate, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult filter")
class LazyResult_filter_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final Predicate<String> isAcceptable = s -> fail("Should not happen");
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<String, Integer> result = lazy.filter(isAcceptable, mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_eventually_evaluate_predicate_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> success(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Predicate<String> isAcceptable = s -> false;
        final Function<String, String> mapper = s -> FAILURE;
        // When
        final Result<String, String> result = lazy.filter(isAcceptable, mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }

    @Test
    void should_not_be_lazy_when_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> success(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Predicate<String> isAcceptable = s -> false;
        final Function<String, String> mapper = s -> FAILURE;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.filter(isAcceptable, mapper);
        // Then
        assertThat(result).isNotInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }
}
