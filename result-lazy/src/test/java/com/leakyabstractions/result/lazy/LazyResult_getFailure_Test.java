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

import static com.leakyabstractions.result.test.Results.failure;
import static com.leakyabstractions.result.test.Results.success;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#getFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult getFailure")
class LazyResult_getFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_empty() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success("SUCCESS"));
        // When
        final Optional<Integer> failure = lazy.getFailure();
        // Then
        assertThat(failure).isEmpty();
    }

    @Test
    void should_return_failure() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        // String
        final Optional<String> failure = lazy.getFailure();
        // Then
        assertThat(failure).containsSame(FAILURE);
    }
}
