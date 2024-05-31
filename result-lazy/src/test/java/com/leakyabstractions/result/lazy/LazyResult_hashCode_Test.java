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

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#hashCode()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult hashCode")
class LazyResult_hashCode_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_same_hash_code_as_the_supplier() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> fail("Should not happen");
        final Result<String, Integer> lazy = new LazyResult<>(supplier);
        // Then
        assertThat(lazy).hasSameHashCodeAs(supplier);
    }

    @Test
    void should_return_same_hash_code_as_the_supplier_after_supplied() {
        // Given
        final Supplier<Result<String, Integer>> supplier = () -> success(SUCCESS);
        final LazyResult<String, Integer> lazy = new LazyResult<>(supplier);
        // When
        lazy.getSupplied();
        // Then
        assertThat(lazy).hasSameHashCodeAs(supplier);
    }
}
