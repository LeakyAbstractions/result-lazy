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
 * <li>{@link com.leakyabstractions.result.Result#filter(java.util.function.Predicate, java.util.function.Function)
 * filter}</li>
 * <li>{@link com.leakyabstractions.result.Result#fallBack(java.util.function.Predicate, java.util.function.Function)
 * fallBack}</li>
 * <li>{@link com.leakyabstractions.result.Result#map(java.util.function.Function, java.util.function.Function)
 * map}</li>
 * <li>{@link com.leakyabstractions.result.Result#mapSuccess(java.util.function.Function) mapSuccess}</li>
 * <li>{@link com.leakyabstractions.result.Result#mapFailure(java.util.function.Function) mapFailure}</li>
 * <li>{@link com.leakyabstractions.result.Result#flatMap(java.util.function.Function, java.util.function.Function)
 * flatMap}</li>
 * <li>{@link com.leakyabstractions.result.Result#flatMapSuccess(java.util.function.Function) flatMapSuccess}</li>
 * <li>{@link com.leakyabstractions.result.Result#flatMapFailure(java.util.function.Function) flatMapFailure}</li>
 * </ul>
 * <p>
 * On the other hand, the supplier will be invoked if any of these <em>terminal operations</em> is performed on a lazy
 * result:
 * <ul>
 * <li>{@link com.leakyabstractions.result.Result#getSuccess() getSuccess}</li>
 * <li>{@link com.leakyabstractions.result.Result#getFailure() getFailure}</li>
 * <li>{@link com.leakyabstractions.result.Result#hasSuccess() hasSuccess}</li>
 * <li>{@link com.leakyabstractions.result.Result#hasFailure() hasFailure}</li>
 * <li>{@link com.leakyabstractions.result.Result#orElse(Object) orElse}</li>
 * <li>{@link com.leakyabstractions.result.Result#orElseMap(java.util.function.Function) orElseMap}</li>
 * <li>{@link com.leakyabstractions.result.Result#stream() stream}</li>
 * </ul>
 * <p>
 * Finally, conditional actions will be performed immediately unless they are
 * {@link com.leakyabstractions.result.lazy.LazyConsumer#of(java.util.function.Consumer) lazy} too:
 * <ul>
 * <li>{@link com.leakyabstractions.result.Result#ifSuccess(java.util.function.Consumer) ifSuccess}</li>
 * <li>{@link com.leakyabstractions.result.Result#ifSuccess(java.util.function.Consumer) ifSuccessOrElse}</li>
 * <li>{@link com.leakyabstractions.result.Result#ifFailure(java.util.function.Consumer) ifFailure}</li>
 * </ul>
 * <p>
 * Once a lazy result retrieves the supplied result, all future operations will be performed immediately and the
 * returned Result objects should not be lazy.
 *
 * @author Guillermo Calvo
 * @see com.leakyabstractions.result
 * @see com.leakyabstractions.result.lazy.LazyResults
 */

package com.leakyabstractions.result.lazy;
