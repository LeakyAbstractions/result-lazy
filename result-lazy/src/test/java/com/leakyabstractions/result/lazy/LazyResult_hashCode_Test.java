
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
