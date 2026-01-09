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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#recover(Predicate, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult recover")
class LazyResult_recover_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final Predicate<String> isRecoverable = s -> fail("Should not happen");
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<Integer, String> result = lazy.recover(isRecoverable, mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_eventually_evaluate_predicate_when_needed() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> failure(FAILURE);
        final Result<String, String> lazy = new LazyResult<>(supplier);
        final Predicate<String> isRecoverable = s -> true;
        final Function<String, String> mapper = s -> SUCCESS;
        // When
        final Result<String, String> result = lazy.recover(isRecoverable, mapper);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .extracting("success", OPTIONAL)
                .containsSame(SUCCESS);
    }

    @Test
    void should_not_be_lazy_when_already_supplied() {
        // Given
        final Supplier<Result<String, String>> supplier = () -> failure(FAILURE);
        final LazyResult<String, String> lazy = new LazyResult<>(supplier);
        final Predicate<String> isRecoverable = s -> true;
        final Function<String, String> mapper = s -> SUCCESS;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.recover(isRecoverable, mapper);
        // Then
        assertThat(result)
                .isNotInstanceOf(LazyResult.class)
                .extracting("success", OPTIONAL)
                .containsSame(SUCCESS);
    }
}
