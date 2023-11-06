
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#ifSuccess(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult ifSuccess")
class LazyResult_ifSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_be_lazy() {
        final Result<String, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_not_be_lazy_and_ignore_success_action() {
        // Given
        final Result<String, String> supplied = failure("FAILURE");
        final Result<String, String> lazy = new LazyResult<>(() -> supplied);
        final Consumer<String> successAction = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(supplied);
    }

    @Test
    void should_not_be_lazy_and_perform_success_action() {
        final Result<String, String> supplied = success(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> successAction = s -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(supplied);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_eventually_perform_action() {
        final Result<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy).hasSuccessSameAs(SUCCESS);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_perform_action_if_already_supplied() {
        final Result<String, String> supplied = success(SUCCESS);
        final LazyResult<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> actionPerformed.set(true);
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.ifSuccess(successAction);
        // Then
        assertThat(result).isSameAs(supplied);
        assertThat(actionPerformed).isTrue();
    }
}
