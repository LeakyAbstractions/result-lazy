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

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#streamSuccess()}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult streamSuccess")
class LazyResult_streamSuccess_Test {

    private static final String SUCCESS = "SUCCESS";

    @Test
    void should_return_non_empty_stream() {
        // Given
        final Result<String, Integer> lazy = new LazyResult<>(() -> success(SUCCESS));
        // When
        final Stream<String> stream = lazy.streamSuccess();
        // Then
        assertThat(stream).singleElement().isSameAs(SUCCESS);
    }

    @Test
    void should_return_empty_stream() {
        // Given
        final Result<Integer, String> lazy = new LazyResult<>(() -> failure("FAILURE"));
        // When
        final Stream<Integer> stream = lazy.streamSuccess();
        // Then
        assertThat(stream).isEmpty();
    }
}
