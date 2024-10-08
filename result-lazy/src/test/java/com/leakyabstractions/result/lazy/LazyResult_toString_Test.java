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

import static com.leakyabstractions.result.test.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#toString()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult toString")
class LazyResult_toString_Test {

    @Test
    void should_return_expected_string_when_not_supplied() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // Then
        assertThat(lazy).hasToString("LazyResult[Not supplied]");
    }

    @Test
    void should_return_expected_string_when_supplied() {
        // Given
        final Result<String, Integer> supplied = success("OK");
        final LazyResult<String, Integer> lazy = new LazyResult<>(() -> supplied);
        // When
        lazy.getSupplied();
        // Then
        assertThat(lazy).hasToString("LazyResult[%s]", supplied);
    }
}
