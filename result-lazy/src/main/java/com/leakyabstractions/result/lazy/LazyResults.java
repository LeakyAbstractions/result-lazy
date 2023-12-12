/*
 * Copyright 2023 Guillermo Calvo
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

import static java.util.Objects.requireNonNull;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.leakyabstractions.result.api.Result;

/**
 * This class consists exclusively of static methods that return lazy {@link Result} instances.
 *
 * @author Guillermo Calvo
 * @see com.leakyabstractions.result
 * @see com.leakyabstractions.result.lazy
 */
public class LazyResults {

    private LazyResults() {
        /** Suppresses default constructor, ensuring non-instantiability */
    }

    /**
     * Creates a new lazy result based on the given result supplier.
     *
     * @apiNote The {@code supplier} is not allowed to return {@code null} or throw any exceptions. If it does, then the
     *     lazy result will relay the thrown exception (or {@code
     *     NullPointerException}) to its caller when/if it needs to be evaluated. If the lazy result needs to be
     *     evaluated again, it will throw {@link NoSuchElementException} (instead of trying to invoke {@code supplier}
     *     again). This ensures that {@code supplier} will be invoked at most once.
     * @param <S> the success type of the result
     * @param <F> the failure type of the result
     * @param supplier the function that supplies the actual result
     * @return the new lazy result
     * @throws NullPointerException if {@code supplier} is {@code null}
     * @see LazyConsumer#of(Consumer)
     */
    public static <S, F> Result<S, F> ofSupplier(Supplier<Result<S, F>> supplier) {
        requireNonNull(supplier, "supplier");
        return new LazyResult<>(supplier);
    }
}
