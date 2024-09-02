/*
 * Copyright 2024 Guillermo Calvo
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
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;

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
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .isNotSameAs(lazy)
                .extracting("success", OPTIONAL)
                .containsSame(SUCCESS);
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
