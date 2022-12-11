
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResult#ifFailure(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult ifFailure")
class LazyResult_ifFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        final Result<String, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_not_be_lazy_and_ignore_failure_action() {
        // Given
        final Result<String, String> supplied = success("SUCCESS");
        final Result<String, String> lazy = new LazyResult<>(() -> supplied);
        final Consumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(supplied);
    }

    @Test
    void should_not_be_lazy_and_perform_failure_action() {
        final Result<String, String> supplied = failure(FAILURE);
        final Result<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(supplied);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_eventually_perform_action() {
        final Result<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final Result<?, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy).hasFailure(FAILURE);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_perform_action_if_already_supplied() {
        final Result<String, String> supplied = failure(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        lazy.getSupplied();
        final Result<?, String> result = lazy.ifFailure(failureAction);
        // Then
        assertThat(result).isSameAs(supplied);
        assertThat(actionPerformed).isTrue();
    }
}
