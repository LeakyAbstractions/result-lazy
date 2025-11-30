/*
 * Copyright 2025 Guillermo Calvo
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#equals(Object)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult equals")
class LazyResult_equals_Test {

    @Test
    void should_be_equal_to_itself() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> fail("Should not happen"));
        // When
        final boolean equals = lazy.equals(lazy);
        // Then
        assertThat(equals).isTrue();
    }

    @Test
    void should_be_equal_to_an_async_result_with_the_same_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy1 = new LazyResult<>(supplier);
        final Result<String, Integer> lazy2 = new LazyResult<>(supplier);
        // Then
        assertThat(lazy1).isEqualTo(lazy2);
    }

    @Test
    void should_not_be_equal_to_the_same_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // Then
        assertThat(lazy).isNotEqualTo(supplier);
    }

    @Test
    void should_not_be_equal_to_an_async_result_with_a_different_supplier() {
        // Given
        final Result<String, Integer> lazy1 = new LazyResult<>(() -> fail("Should not happen"));
        final Result<String, Integer> lazy2 = new LazyResult<>(() -> fail("Should not happen"));
        // Then
        assertThat(lazy1).isNotEqualTo(lazy2);
    }
}
