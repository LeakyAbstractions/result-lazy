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
import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#mapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult mapSuccess")
class LazyResult_mapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";
    private static final String ANOTHER = "ANOTHER";

    @Test
    void should_be_lazy() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Function<String, Integer> mapper = s -> fail("Should not happen");
        // When
        final Result<Integer, String> result = lazy.mapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class);
    }

    @Test
    void should_eventually_map_when_needed() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Function<String, String> mapper = s -> ANOTHER;
        // When
        final Result<String, String> result = lazy.mapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasSuccessSameAs(ANOTHER);
    }

    @Test
    void should_ignore_success_mapping() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final Function<String, String> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.mapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final LazyResult<Integer, String> lazy = new LazyResult<>(() -> success(123));
        final Function<Integer, String> mapper = s -> SUCCESS;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.mapSuccess(mapper);
        // Then
        assertThat(result).isNotInstanceOf(LazyResult.class).hasSuccessSameAs(SUCCESS);
    }
}
