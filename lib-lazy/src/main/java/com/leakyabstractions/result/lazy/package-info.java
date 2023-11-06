/**
 * A Java library to handle success and failure without exceptions
 * <h2>Lazy Result Library for Java</h2>
 * <p>
 * Lazy results can be manipulated just like any other result; they will try to defer the invocation of the given
 * supplier as long as possible. The purpose is to encapsulate an expensive operation that may be omitted if there's no
 * actual need to examine the result.
 * <p>
 * Lazy results can be <em>screened</em> and <em>transformed</em> without actually performing the expensive operation:
 * <ul>
 * <li>{@link com.leakyabstractions.result.api.Result#filter(java.util.function.Predicate, java.util.function.Function)
 * filter}
 * <li>{@link com.leakyabstractions.result.api.Result#recover(java.util.function.Predicate, java.util.function.Function)
 * recover}
 * <li>{@link com.leakyabstractions.result.api.Result#map(java.util.function.Function, java.util.function.Function) map}
 * <li>{@link com.leakyabstractions.result.api.Result#mapSuccess(java.util.function.Function) mapSuccess}
 * <li>{@link com.leakyabstractions.result.api.Result#mapFailure(java.util.function.Function) mapFailure}
 * <li>{@link com.leakyabstractions.result.api.Result#flatMap(java.util.function.Function, java.util.function.Function)
 * flatMap}
 * <li>{@link com.leakyabstractions.result.api.Result#flatMapSuccess(java.util.function.Function) flatMapSuccess}
 * <li>{@link com.leakyabstractions.result.api.Result#flatMapFailure(java.util.function.Function) flatMapFailure}
 * </ul>
 * <p>
 * On the other hand, the supplier will be invoked if any of these <em>terminal operations</em> is performed on a lazy
 * result:
 * <ul>
 * <li>{@link com.leakyabstractions.result.api.Result#getSuccess() getSuccess}
 * <li>{@link com.leakyabstractions.result.api.Result#getFailure() getFailure}
 * <li>{@link com.leakyabstractions.result.api.Result#hasSuccess() hasSuccess}
 * <li>{@link com.leakyabstractions.result.api.Result#hasFailure() hasFailure}
 * <li>{@link com.leakyabstractions.result.api.Result#orElse(Object) orElse}
 * <li>{@link com.leakyabstractions.result.api.Result#orElseMap(java.util.function.Function) orElseMap}
 * <li>{@link com.leakyabstractions.result.api.Result#streamSuccess() streamSuccess}
 * <li>{@link com.leakyabstractions.result.api.Result#streamFailure() streamFailure}
 * </ul>
 * <p>
 * Finally, conditional actions will be performed immediately unless they are
 * {@link com.leakyabstractions.result.lazy.LazyConsumer#of(java.util.function.Consumer) lazy} too:
 * <ul>
 * <li>{@link com.leakyabstractions.result.api.Result#ifSuccess(java.util.function.Consumer) ifSuccess}
 * <li>{@link com.leakyabstractions.result.api.Result#ifSuccessOrElse(java.util.function.Consumer, java.util.function.Consumer)
 * ifSuccessOrElse}
 * <li>{@link com.leakyabstractions.result.api.Result#ifFailure(java.util.function.Consumer) ifFailure}
 * </ul>
 * <p>
 * Once a lazy result retrieves the supplied result, all future operations will be performed immediately and the
 * returned Result objects should not be lazy.
 *
 * @author Guillermo Calvo
 * @see com.leakyabstractions.result.api.Result
 * @see com.leakyabstractions.result.lazy.LazyResults
 */

package com.leakyabstractions.result.lazy;
