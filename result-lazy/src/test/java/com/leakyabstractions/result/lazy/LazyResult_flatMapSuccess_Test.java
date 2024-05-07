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
 * Tests for {@link LazyResult#flatMapSuccess(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMapSuccess")
class LazyResult_flatMapSuccess_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final Function<String, Result<String, String>> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Function<String, Result<String, String>> mapper = s -> failure(FAILURE);
        // When
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasFailureSameAs(FAILURE);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final LazyResult<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Result<String, String> another = success("ANOTHER");
        final Function<String, Result<String, String>> mapper = s -> another;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.flatMapSuccess(mapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<String, Void> result = unit(SUCCESS);
        final Function<String, Result<Integer, Void>> fun1 = x -> unit(x.length());
        final Function<Integer, Result<String, Void>> fun2 = x -> unit(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(bind(unit(SUCCESS), fun1))
                .hasSuccess(fun1.apply(SUCCESS).getSuccess().get())
                .hasSuccess(7);
        // Right Identity Law
        assertThat(result)
                .hasSuccess(bind(result, LazyResult_flatMapSuccess_Test::unit).getSuccess().get())
                .hasSuccess(SUCCESS);
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .hasSuccess(
                        bind(result, s -> fun2.apply(fun1.apply(s).getSuccess().get())).getSuccess().get())
                .hasSuccess("70a");
    }

    private static <T> Result<T, Void> unit(T value) {
        return new LazyResult<>(() -> success(value));
    }

    private static <T, T2> Result<T2, Void> bind(
            Result<T, Void> result, Function<T, Result<T2, Void>> function) {
        return result.flatMapSuccess(function);
    }
}
