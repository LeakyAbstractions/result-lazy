
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
 * Tests for {@link LazyResult#flatMap(Function, Function)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyResult flatMap")
class LazyResult_flatMap_Test {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILURE = "FAILURE";

    @Test
    void should_be_lazy() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> fail("Should not happen"));
        final Function<String, Result<String, String>> successMapper = s -> fail("Should not happen");
        final Function<String, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        final Result<String, String> result = lazy.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).isNotSameAs(lazy);
    }

    @Test
    void should_eventually_flat_map_when_needed() {
        // Given
        final Result<String, String> lazy = new LazyResult<>(() -> failure(FAILURE));
        final Function<String, Result<String, String>> successMapper = s -> fail("Should not happen");
        final Function<String, Result<String, String>> failureMapper = f -> success(SUCCESS);
        // When
        final Result<String, String> result = lazy.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isInstanceOf(LazyResult.class).hasSuccessSameAs(SUCCESS);
    }

    @Test
    void should_not_be_lazy_if_already_supplied() {
        // Given
        final LazyResult<String, String> lazy = new LazyResult<>(() -> success(SUCCESS));
        final Result<String, String> another = success("ANOTHER");
        final Function<String, Result<String, String>> successMapper = s -> another;
        final Function<String, Result<String, String>> failureMapper = f -> fail("Should not happen");
        // When
        lazy.getSupplied();
        final Result<String, String> result = lazy.flatMap(successMapper, failureMapper);
        // Then
        assertThat(result).isSameAs(another);
    }

    @Test
    void obeys_monad_laws() {
        // Given
        final Result<String, String> result = unit(SUCCESS);
        final Function<String, Result<Integer, Integer>> fun1 = x -> success(x.length());
        final Function<Integer, Result<String, String>> fun2 = x -> failure(x * 10 + "a");
        // Then
        // Left Identity Law
        assertThat(bind(unit(SUCCESS), fun1))
                .hasSuccess(fun1.apply(SUCCESS).getSuccess().get())
                .hasSuccess(7);
        // Right Identity Law
        assertThat(result)
                .hasSuccess(bind(result, LazyResult_flatMap_Test::unit).getSuccess().get())
                .hasSuccess(SUCCESS);
        // Associativity Law
        assertThat(bind(bind(result, fun1), fun2))
                .hasFailure(
                        bind(result, s -> fun2.apply(fun1.apply(s).getSuccess().get())).getFailure().get())
                .hasFailure("70a");
    }

    private static <T> Result<T, T> unit(T value) {
        return new LazyResult<>(() -> success(value));
    }

    private static <T, T2> Result<T2, T2> bind(
            Result<T, T> result, Function<T, Result<T2, T2>> function) {
        return result.flatMap(function, function);
    }
}
