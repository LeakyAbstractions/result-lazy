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

import static org.assertj.core.api.Assertions.assertThat;
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
