/*
 * Copyright 2026 Guillermo Calvo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.test.Results.failure;
import static com.leakyabstractions.result.test.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

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
        assertThat(lazy)
                .isInstanceOf(LazyResult.class)
                .extracting("success", OPTIONAL)
                .containsSame(SUCCESS);
    }

    @Test
    void should_pass_with_failure_value() {
        // Given
        final Supplier<Result<Integer, String>> supplier = () -> failure(FAILURE);
        // When
        final Result<Integer, String> lazy = LazyResults.ofSupplier(supplier);
        // Then
        assertThat(lazy)
                .isInstanceOf(LazyResult.class)
                .extracting("failure", OPTIONAL)
                .containsSame(FAILURE);
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
