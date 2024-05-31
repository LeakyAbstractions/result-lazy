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

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#streamFailure()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult streamFailure")
class LazyResult_streamFailure_Test {

    private static final String FAILURE = "FAILURE";

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        // When
        final Stream<String> stream = lazy.streamFailure();
        // Then
        assertThat(stream).singleElement().isSameAs(FAILURE);
    }

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success("SUCCESS"));
        // When
        final Stream<Integer> stream = lazy.streamFailure();
        // Then
        assertThat(stream).isEmpty();
    }
}
