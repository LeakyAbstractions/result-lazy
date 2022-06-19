
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResults#ofCallable(Callable)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResults ofCallable")
class LazyResults_ofCallable_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_throw_exception_when_null_callable() {
        // When
        final Throwable thrown = catchThrowable(() -> LazyResults.ofCallable(null));
        // Then
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_pass_when_callable_succeeds() {
        // When
        final Result<String, Exception> lazy = LazyResults.ofCallable(() -> SUCCESS);
        // Then
        assertThat(lazy)
                .isInstanceOf(LazyResult.class)
                .hasSuccessSameAs(SUCCESS);
    }

    @Test
    void should_pass_when_callable_fails() {
        // Given
        final Exception exception = new IllegalArgumentException("Testing");
        // When
        final Result<String, Exception> lazy = LazyResults.ofCallable(() -> {
            throw exception;
        });
        // Then
        assertThat(lazy)
                .isInstanceOf(LazyResult.class)
                .hasFailureSameAs(exception);
    }

    @Test
    void should_invoke_callable_only_once() {
        // Given
        final AtomicBoolean alreadyInvoked = new AtomicBoolean();
        final Callable<String> callable = () -> {
            if (alreadyInvoked.get()) {
                fail("Should not happen");
            }
            return SUCCESS;
        };
        final LazyResult<String, ?> lazy = (LazyResult<String, ?>) LazyResults.ofCallable(callable);
        // Then
        assertThatNoException().isThrownBy(() -> {
            lazy.getSupplied();
            lazy.getSupplied();
            lazy.getSupplied();
        });
    }

    @Test
    void should_invoke_callable_only_once_even_when_it_returns_null() {
        // Given
        final AtomicBoolean alreadyInvoked = new AtomicBoolean();
        final Callable<String> callable = () -> {
            if (alreadyInvoked.get()) {
                fail("Should not happen");
            }
            return null;
        };
        final LazyResult<String, ?> lazy = (LazyResult<String, ?>) LazyResults.ofCallable(callable);
        // Then
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void should_invoke_callable_only_once_even_when_it_fails() {
        // Given
        final AtomicBoolean alreadyInvoked = new AtomicBoolean();
        final Callable<String> callable = () -> {
            if (alreadyInvoked.get()) {
                fail("Should not happen");
            }
            throw new IllegalArgumentException("Testing");
        };
        final LazyResult<String, ?> lazy = (LazyResult<String, ?>) LazyResults.ofCallable(callable);
        // Then
        assertThatNoException().isThrownBy(() -> {
            lazy.getSupplied();
            lazy.getSupplied();
            lazy.getSupplied();
        });
    }
}
