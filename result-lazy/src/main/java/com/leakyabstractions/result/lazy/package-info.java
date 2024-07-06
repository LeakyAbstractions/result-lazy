/**
 * Provides lazy versions of {@link com.leakyabstractions.result.api.Result} objects.
 * <p>
 * <img src="https://dev.leakyabstractions.com/result-api/result.svg" alt="Result Library">
 * <h2>Lazy Results</h2>
 * <p>
 * Lazy results optimize performance by deferring costly operations until absolutely necessary. They behave like regular
 * results, but only execute the underlying operation when an actual check for success or failure is performed.
 *
 * @author <a href="https://guillermo.dev/">Guillermo Calvo</a>
 * @see <a href="https://result.leakyabstractions.com/add-ons/lazy">Quick guide</a>
 * @see <a href="https://github.com/LeakyAbstractions/result-lazy/">Source code</a>
 * @see com.leakyabstractions.result.lazy.LazyResults
 * @see com.leakyabstractions.result.lazy.LazyConsumer
 * @see com.leakyabstractions.result.api.Result
 * @see com.leakyabstractions.result.api
 */

package com.leakyabstractions.result.lazy;
