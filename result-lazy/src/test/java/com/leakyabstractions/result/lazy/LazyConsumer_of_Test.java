
package com.leakyabstractions.result.lazy;

import static com.leakyabstractions.result.assertj.ResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LazyConsumer#of(Consumer)}.
 *
 * @author Guillermo Calvo
 */
@DisplayName("LazyConsumer of")
class LazyConsumer_of_Test {

    @Test
    void should_throw_exception_when_null_consumer() {
        // When
        ThrowingCallable callable = () -> LazyConsumer.of(null);
        // Then
        assertThatThrownBy(callable).isInstanceOf(NullPointerException.class);
    }

    @Test
    void should_pass_when_non_null() {
        // When
        Consumer<String> lazy = LazyConsumer.of(s -> fail("Should not happen"));
        // Then
        assertThat(lazy).isInstanceOf(LazyConsumer.class);
    }

    @Test
    void should_delegate_to_consumer() {
        // Given
        final AtomicBoolean actionPerformed = new AtomicBoolean(false);
        final Consumer<String> consumer = s -> actionPerformed.set(true);
        // When
        LazyConsumer.of(consumer).accept("TEST");
        // Then
        assertThat(actionPerformed).isTrue();
    }
}
