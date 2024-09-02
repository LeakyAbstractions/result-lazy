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

import static java.util.Objects.requireNonNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.leakyabstractions.result.api.Result;

/**
 * Represents an operation that lazily consumes the success or failure of a {@link Result}.
 * <p>
 * This functional interface is expected to operate via side effects. It should be used with
 * {@link Result#ifSuccess(Consumer) ifSuccess}, {@link Result#ifFailure(Consumer) ifFailure} and
 * {@link Result#ifSuccessOrElse(Consumer, Consumer) ifSuccessOrElse} when those side effects can be skipped.
 * <p>
 * Unlike regular {@link Consumer} instances, lazy ones won't force a lazy result to retrieve the backing result from
 * its {@link LazyResults#ofSupplier(Supplier) supplier}. In other words, these consumers will be ignored if the lazy
 * result never evaluates to success or failure.
 * <p>
 * If a lazy consumer is used with a non-lazy result, or to a lazy result that has already retrieved the backing result
 * from its supplier, then the operation will be executed immediately.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @param <T> the type of the input to the operation
 * @see com.leakyabstractions.result.lazy Lazy Results
 * @see LazyConsumer#of(Consumer) Creating lazy consumers
 */
@FunctionalInterface
public interface LazyConsumer<T> extends Consumer<T> {

    /**
     * Returns a composed lazy consumer that performs, in sequence, this lazy operation followed by the {@code after}
     * operation.
     * <p>
     * If performing either operation throws an exception, it is relayed to the caller of the composed operation. If
     * performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed lazy consumer that performs in sequence this lazy operation followed by the {@code after}
     *     operation
     * @throws NullPointerException if {@code after} is null
     */
    @Override
    default LazyConsumer<T> andThen(Consumer<? super T> after) {
        requireNonNull(after, "after");
        return Consumer.super.andThen(after)::accept;
    }

    /**
     * Creates a new lazy consumer based on a regular one.
     *
     * @param <T> the type of the input to the action
     * @param consumer the action to be applied to this result's success or failure value
     * @return the new lazy consumer
     * @see LazyResults
     */
    static <T> LazyConsumer<T> of(Consumer<? super T> consumer) {
        requireNonNull(consumer, "consumer");
        return consumer::accept;
    }
}
