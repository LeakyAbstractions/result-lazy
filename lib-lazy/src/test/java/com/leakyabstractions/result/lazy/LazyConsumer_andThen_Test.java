
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyConsumer#andThen(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyConsumer andThen")
class LazyConsumer_andThen_Test {

    @Test
    void should_be_lazy() {
        // Given
        final LazyConsumer<String> action1 = f -> fail("Should not happen");
        final Consumer<String> action2 = f -> fail("Should not happen");
        // When
        final Consumer<String> composedAction = action1.andThen(action2);
        // Then
        assertThat(composedAction).isInstanceOf(LazyConsumer.class);
    }
}
