
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.Results.failure;
import static com.leakyabstractions.result.Results.success;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.assertj.ResultAssertions.assertThatNoException;
import static com.leakyabstractions.result.assertj.ResultAssertions.fail;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.Result;

/**
 * Tests for {@link LazyResults#ofSupplier(Supplier)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResults ofSupplier")
class LazyResults_ofSupplier_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_throw_exception_when_null_supplier() {
        // When
        ThrowableAssert.ThrowingCallable callable = () -> LazyResults.ofSupplier(null);
        // Then
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_throw_exception_when_supplier_returns_null() {
        // Given
        final LazyResult<?, ?> lazy = (LazyResult<?, ?>) LazyResults.ofSupplier(() -> null);
        // When
        ThrowableAssert.ThrowingCallable callable = () -> lazy.getSupplied();
        // Then
        assertThatThrownBy(callable).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void should_pass_with_success_value() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> success(SUCCESS);
        // When
        final Result<String, Integer> lazy = LazyResults.ofSupplier(supplier);
        // Then
        assertThat(lazy).isInstanceOf(LazyResult.class).hasSuccessSameAs(SUCCESS);
    }

    @Test
    void should_pass_with_failure_value() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> failure(FAILURE);
        // When
        final Result<Integer, String> lazy = LazyResults.ofSupplier(supplier);
        // Then
        assertThat(lazy).isInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }

    @Test
    void should_invoke_supplier_only_once() {
        // Given
        final AtomicBoolean alreadyInvoked = new AtomicBoolean();
        final Supplier<Result<String, Long>> supplier = () -> {
            if (alreadyInvoked.get()) {
                fail("Should not happen");
            }
            return success(SUCCESS);
        };
        final LazyResult<String, ?> lazy = (LazyResult<String, ?>) LazyResults.ofSupplier(supplier);
        // Then
        assertThatNoException()
                .isThrownBy(
                        () -> {
                            lazy.getSupplied();
                            lazy.getSupplied();
                            lazy.getSupplied();
                        });
    }

    @Test
    void should_invoke_supplier_only_once_even_when_it_returns_null() {
        // Given
        final AtomicBoolean alreadyInvoked = new AtomicBoolean();
        final Supplier<Result<String, Long>> supplier = () -> {
            if (alreadyInvoked.get()) {
                fail("Should not happen");
            }
            return null;
        };
        final LazyResult<String, ?> lazy = (LazyResult<String, ?>) LazyResults.ofSupplier(supplier);
        // Then
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void should_invoke_supplier_only_once_even_when_it_fails() {
        // Given
        final AtomicBoolean alreadyInvoked = new AtomicBoolean();
        final Supplier<Result<String, Long>> supplier = () -> {
            if (alreadyInvoked.get()) {
                fail("Should not happen");
            }
            throw new IllegalArgumentException("Testing");
        };
        final LazyResult<String, ?> lazy = (LazyResult<String, ?>) LazyResults.ofSupplier(supplier);
        // Then
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(lazy::getSupplied).isInstanceOf(NoSuchElementException.class);
    }
}
