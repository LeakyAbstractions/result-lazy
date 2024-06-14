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

import static com.leakyabstractions.result.core.Results.failure;
import static com.leakyabstractions.result.core.Results.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;

import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.leakyabstractions.result.api.Result;

/**
 * Tests for {@link LazyResult#flatMapFailure(Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMapFailure")
class LazyResult_flatMapFailure_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final Function<String, Result<String, String>> mapper = s -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMapFailure(mapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final Function<String, Result<String, String>> mapper = s -> success(SUCCESS);
        // When
        final Result<String, String> result = lazy.flatMapFailure(mapper);
        // Then
        assertThat(result)
                .isInstanceOf(LazyResult.class)
                .extracting("success", OPTIONAL)
                .containsSame(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final LazyResult<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final Result<String, String> another = failure("ANOTHER");
        final Function<String, Result<String, String>> mapper = s -> another;
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.flatMapFailure(mapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<Void, String> result = unit(FAILURE);
        final Function<String, Result<Void, Integer>> fun1 = x -> unit(x.length());
        final Function<Integer, Result<Void, String>> fun2 = x -> unit(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(bind(unit(FAILURE), fun1))
                .extracting("failure", OPTIONAL)
                .contains(fun1.apply(FAILURE).getFailure().get())
                .contains(7);
        // Right Identity Law
        assertThat(result)
                .extracting("failure", OPTIONAL)
                .contains(bind(result, LazyResult_flatMapFailure_Test::unit).getFailure().get())
                .contains(FAILURE);
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .extracting("failure", OPTIONAL)
                .contains(
                        bind(result, s -> fun2.apply(fun1.apply(s).getFailure().get())).getFailure().get())
                .contains("70a");
    }

    private static <T> Result<Void, T> unit(T value) {
        return new LazyResult<>(() -> failure(value));
    }

    private static <T, T2> Result<Void, T2> bind(
            Result<Void, T> result, Function<T, Result<Void, T2>> function) {
        return result.flatMapFailure(function);
    }
}
