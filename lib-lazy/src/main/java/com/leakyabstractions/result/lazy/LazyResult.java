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

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.leakyabstractions.result.api.Result;

/**
 * Lazy implementation of a {@link Result}.
 *
 * @author Guillermo Calvo
 * @param <S> the type of the success value
 * @param <F> the type of the failure value
 */
final class LazyResult<S, F> implements Result<S, F> {

    private final Supplier<Result<S, F>> supplier;
    private final AtomicReference<Result<S, F>> supplied = new AtomicReference<>();
    private volatile boolean isNotSupplied = true;

    LazyResult(Supplier<Result<S, F>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Optional<S> getSuccess() {
        return this.getSupplied().getSuccess();
    }

    @Override
    public Optional<F> getFailure() {
        return this.getSupplied().getFailure();
    }

    @Override
    public boolean hasSuccess() {
        return this.getSupplied().hasSuccess();
    }

    @Override
    public boolean hasFailure() {
        return this.getSupplied().hasFailure();
    }

    @Override
    public S orElse(S other) {
        return this.getSupplied().orElse(other);
    }

    @Override
    public S orElseMap(Function<? super F, ? extends S> mapper) {
        return this.getSupplied().orElseMap(mapper);
    }

    @Override
    public Stream<S> streamSuccess() {
        return this.getSupplied().streamSuccess();
    }

    @Override
    public Stream<F> streamFailure() {
        return this.getSupplied().streamFailure();
    }

    @Override
    public Result<S, F> ifSuccess(Consumer<? super S> action) {
        return lazily(
                this.isNotSupplied && action instanceof LazyConsumer,
                () -> this.getSupplied().ifSuccess(action));
    }

    @Override
    public Result<S, F> ifSuccessOrElse(Consumer<? super S> s, Consumer<? super F> f) {
        return lazily(
                this.isNotSupplied && s instanceof LazyConsumer && f instanceof LazyConsumer,
                () -> this.getSupplied().ifSuccessOrElse(s, f));
    }

    @Override
    public Result<S, F> ifFailure(Consumer<? super F> action) {
        return lazily(
                this.isNotSupplied && action instanceof LazyConsumer,
                () -> this.getSupplied().ifFailure(action));
    }

    @Override
    public Result<S, F> filter(
            Predicate<? super S> isAcceptable, Function<? super S, ? extends F> mapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().filter(isAcceptable, mapper));
    }

    @Override
    public Result<S, F> recover(
            Predicate<? super F> isRecoverable, Function<? super F, ? extends S> mapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().recover(isRecoverable, mapper));
    }

    @Override
    public <S2, F2> Result<S2, F2> map(
            Function<? super S, ? extends S2> successMapper,
            Function<? super F, ? extends F2> failureMapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().map(successMapper, failureMapper));
    }

    @Override
    public <S2> Result<S2, F> mapSuccess(Function<? super S, ? extends S2> mapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().mapSuccess(mapper));
    }

    @Override
    public <F2> Result<S, F2> mapFailure(Function<? super F, ? extends F2> mapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().mapFailure(mapper));
    }

    @Override
    public <S2, F2> Result<S2, F2> flatMap(
            Function<? super S, ? extends Result<? extends S2, ? extends F2>> successMapper,
            Function<? super F, ? extends Result<? extends S2, ? extends F2>> failureMapper) {
        return lazily(
                this.isNotSupplied, () -> this.getSupplied().flatMap(successMapper, failureMapper));
    }

    @Override
    public <S2> Result<S2, F> flatMapSuccess(
            Function<? super S, ? extends Result<? extends S2, ? extends F>> mapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().flatMapSuccess(mapper));
    }

    @Override
    public <F2> Result<S, F2> flatMapFailure(
            Function<? super F, ? extends Result<? extends S, ? extends F2>> mapper) {
        return lazily(this.isNotSupplied, () -> this.getSupplied().flatMapFailure(mapper));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LazyResult)) return false;
        return this.supplier.equals(((LazyResult<?, ?>) obj).supplier);
    }

    @Override
    public int hashCode() {
        return this.supplier.hashCode();
    }

    @Override
    public String toString() {
        if (this.isNotSupplied) {
            return "LazyResult[Not supplied]";
        }
        return new StringBuilder("LazyResult[").append(this.supplied.get()).append("]").toString();
    }

    Result<S, F> getSupplied() {
        if (this.isNotSupplied) {
            synchronized (this) {
                // Double-checked locking, functional style
                Optional.of(this.isNotSupplied).filter(Boolean::booleanValue).ifPresent(this::supply);
            }
        }
        final Result<S, F> result = this.supplied.get();
        if (result == null) {
            throw new NoSuchElementException("The supplied result was null");
        }
        return result;
    }

    private void supply(Boolean ignore) {
        this.isNotSupplied = false;
        this.supplied.set(this.supplier.get());
    }

    private static <S2, F2> Result<S2, F2> lazily(boolean lazily, Supplier<Result<S2, F2>> supplier) {
        return lazily ? new LazyResult<>(supplier) : supplier.get();
    }
}
