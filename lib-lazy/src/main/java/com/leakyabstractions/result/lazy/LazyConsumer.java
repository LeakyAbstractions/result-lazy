/*
 * Copyright 2022 Guillermo Calvo
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

import com.leakyabstractions.result.Result;

/**
 * Represents an operation to be <em>lazily</em> performed with the success/failure value of a result.
 * <p>
 * Unlike regular ones, lazy consumers won't force a {@link com.leakyabstractions.result.lazy lazy result} to retrieve
 * the backing result from its supplier. In other words, they may be ignored if the lazy result ends up not invoking its
 * result supplier.
 * <p>
 * Instances of objects implementing this interface are intended to be passed as parameters to
 * {@link Result#ifSuccess(Consumer)}, {@link Result#ifSuccessOrElse(Consumer, Consumer)} and
 * {@link Result#ifFailure(Consumer)} when the operation only needs to be performed if the lazy result eventually
 * retrieves an actual result from its supplier.
 * <p>
 * If a lazy consumer is passed to a non-lazy result, or to a lazy result that has already retrieved the backing result
 * from its supplier, then the operation will be executed immediately. On the other hand, if the result object returned
 * by {@code ifSuccess}, {@code ifSuccessOrElse} or {@code ifFailure} is discarded, the lazy consumer will never be
 * evaluated.
 * <p>
 * This is a functional interface whose functional method is {@link #accept(Object)} and it is expected to operate via
 * side effects.
 *
 * @author Guillermo Calvo
 * @param <T> the type of the input to the operation
 * @see LazyConsumer#of(Consumer)
 */
@FunctionalInterface
public interface LazyConsumer<T> extends Consumer<T> {

    /**
     * Returns a composed {@code LazyConsumer} that performs, in sequence, this lazy operation followed by the
     * {@code after} operation. If performing either operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception, the {@code after} operation will not be
     * performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code LazyConsumer} that performs in sequence this lazy operation followed by the
     *     {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    @Override
    default LazyConsumer<T> andThen(Consumer<? super T> after) {
        requireNonNull(after, "after");
        return Consumer.super.andThen(after)::accept;
    }

    /**
     * Creates a new lazy consumer based on a regular one.
     * <p>
     * Lazy consumers encapsulate actions that depend on success or failure and can be safely deferred or even
     * completely ignored if a lazy result is never evaluated. They are intended to be passed as parameters to:
     * <ul>
     * <li>{@link Result#ifSuccess(Consumer) ifSuccess}
     * <li>{@link Result#ifSuccessOrElse(Consumer, Consumer) ifSuccessOrElse}
     * <li>{@link Result#ifFailure(Consumer) ifFailure}
     * </ul>
     * <p>
     * These actions will execute immediately if passed to non-lazy results.
     *
     * @param <T> the type of the input to the action
     * @param consumer the action to be applied to this result's success value
     * @return the new lazy consumer
     * @see com.leakyabstractions.result.lazy
     * @see LazyResults
     */
    static <T> LazyConsumer<T> of(Consumer<? super T> consumer) {
        requireNonNull(consumer, "consumer");
        return consumer::accept;
    }
}
