
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
 * Tests for {@link LazyResult#ifSuccessOrElse(Consumer, Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult ifSuccessOrElse")
class LazyResult_ifSuccessOrElse_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        final Result<String, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_not_be_lazy_and_perform_success_action_only() {
        final Result<String, String> supplied = success(SUCCESS);
        final Result<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> successAction = s -> actionPerformed.set(true);
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(supplied);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_not_be_lazy_and_perform_failure_action_only() {
        final Result<String, String> failure = failure(FAILURE);
        final Result<String, String> lazy = new LazyResult<>(() -> failure);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        final Consumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isSameAs(failure);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_eventually_perform_action() {
        final Result<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> actionPerformed.set(true);
        final LazyConsumer<String> failureAction = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy).hasSuccessSameAs(SUCCESS);
        assertThat(actionPerformed).isTrue();
    }

    @Test
    void should_perform_action_if_already_supplied() {
        final Result<String, String> supplied = failure(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(() -> supplied);
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final LazyConsumer<String> successAction = s -> fail("Should not happen");
        final LazyConsumer<String> failureAction = f -> actionPerformed.set(true);
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.ifSuccessOrElse(successAction, failureAction);
        // Then
        assertThat(actionPerformed).isTrue();
        assertThat(result).isSameAs(supplied);
    }
}
