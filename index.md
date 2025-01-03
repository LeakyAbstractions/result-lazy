---
title: Lazy Results
description: Result-Lazy provides lazy versions of Result objects
image: https://dev.leakyabstractions.com/result/result-banner.png
---

# Lazy Results

This library provides lazy versions of [Result objects][RESULT].

Lazy results optimize performance by deferring costly operations until absolutely necessary. They behave like regular
results, but only execute the underlying operation when an actual check for success or failure is performed.


## Adding Lazy Results to Your Build

Artifact coordinates:

- Group ID: `com.leakyabstractions`
- Artifact ID: `result-lazy`
- Version: `{{ site.current_version }}`

[Maven Central Repository][ARTIFACTS] provides snippets for different build tools to declare this dependency.


## Creating Lazy Results

We can use [`LazyResults::ofSupplier`][OF_SUPPLIER] to create a lazy result.

```java
{% include_relative result-lazy/src/test/java/example/Fragments.java fragment="creation" %}
```

While [suppliers][SUPPLIER] can return a fixed success or failure, lazy results shine when they encapsulate
time-consuming or resource-intensive operations.

```java
{% include_relative result-lazy/src/test/java/example/Fragments.java fragment="expensive_calculation" %}
```

This sample method simply increments and returns a counter for brevity. However, in a typical scenario, this would
involve an I/O operation.


## Skipping Expensive Calculations

The advantage of lazy results is that they defer invoking the provided [`Supplier`][SUPPLIER] for as long as possible.
Despite this, you can screen and transform them like any other result without losing their laziness.

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_skip_expensive_calculation" %}
```

In this example, the expensive calculation is omitted because the lazy result is never fully evaluated. This test
demonstrates that a lazy result can be transformed while maintaining laziness, ensuring that the expensive calculation
is deferred.

> These methods will preserve laziness:
> 
> - [`Result::filter`][RESULT_FILTER]
> - [`Result::recover`][RESULT_RECOVER]
> - [`Result::mapSuccess`][RESULT_MAP_SUCCESS]
> - [`Result::mapFailure`][RESULT_MAP_FAILURE]
> - [`Result::map`][RESULT_MAP]
> - [`Result::flatMapSuccess`][RESULT_FLAT_MAP_SUCCESS]
> - [`Result::flatMapFailure`][RESULT_FLAT_MAP_FAILURE]
> - [`Result::flatMap`][RESULT_FLAT_MAP]


## Triggering Result Evaluation

Finally, when it's time to check whether the operation succeeds or fails, the lazy result will execute it. This is
triggered by using any of the _terminal_ methods, such as [`Result::hasSuccess`][RESULT_HAS_SUCCESS].

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_execute_expensive_calculation" %}
```

Here, the expensive calculation is executed because the lazy result is finally evaluated.

> Terminal methods will immediately evaluate the lazy result:
> 
> - [`Result::hasSuccess`][RESULT_HAS_SUCCESS]
> - [`Result::hasFailure`][RESULT_HAS_FAILURE]
> - [`Result::getSuccess`][RESULT_GET_SUCCESS]
> - [`Result::getFailure`][RESULT_GET_FAILURE]
> - [`Result::orElse`][RESULT_OR_ELSE]
> - [`Result::orElseMap`][RESULT_OR_ELSE_MAP]
> - [`Result::streamSuccess`][RESULT_STREAM_SUCCESS]
> - [`Result::streamFailure`][RESULT_STREAM_FAILURE]


## Handling Success and Failure Eagerly

By default, [`Result::ifSuccess`][RESULT_IF_SUCCESS], [`Result::ifFailure`][RESULT_IF_FAILURE], and
[`Result::ifSuccessOrElse`][RESULT_IF_SUCCESS_OR_ELSE] are treated as terminal methods. This means they eagerly evaluate
the result and then perform an action based on its status.

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_handle_success_eagerly" %}
```

In this test, we don't explicitly _unwrap the value_ or _check the status_, but since we want to
_consume the success value_, we need to evaluate the lazy result first.

Furthermore, even if we wanted to handle the failure scenario, we would still need to evaluate the lazy result.

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_handle_failure_eagerly" %}
```

In this other test, we use [`Result::ifFailure`][RESULT_IF_FAILURE] instead of [`Result::ifSuccess`][RESULT_IF_SUCCESS].
Since the lazy result is evaluated to a success, the failure consumer is never executed.

> These methods are treated as terminal when used with regular consumer functions:
> 
> - [`Result::ifSuccess`][RESULT_IF_SUCCESS]
> - [`Result::ifFailure`][RESULT_IF_FAILURE]
> - [`Result::ifSuccessOrElse`][RESULT_IF_SUCCESS_OR_ELSE]


## Handling Success and Failure Lazily

When these conditional actions may also be skipped along with the expensive calculation, we can encapsulate them into a
[`LazyConsumer`][LAZY_CONSUMER] instead of a regular [`Consumer`][CONSUMER]. All we need to do is to create the consumer
using [`LazyConsumer::of`][LAZY_CONSUMER_OF]. Lazy consumers will preserve the laziness until a terminal method is
eventually used on the result.

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_handle_success_lazily" %}
```

Here, we use a lazy consumer with [`Result::ifSuccess`][RESULT_IF_SUCCESS] so the expensive calculation is skipped
because the lazy result is never fully evaluated.

The full source code for the examples is [available on GitHub][SOURCE_CODE].


# Additional Info

## Releases

This library adheres to [Pragmatic Versioning][PRAGVER].

Artifacts are available in [Maven Central][ARTIFACTS].


## Javadoc

Here you can find the full [Javadoc documentation][JAVADOC].


## Looking for Support?

We'd love to help. Check out the [support guidelines][SUPPORT].


## Contributions Welcome

If you'd like to contribute to this project, please [start here][CONTRIBUTING].


## Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct][CODE_OF_CONDUCT].
By participating, you are expected to uphold this code.


## Author

Copyright 2025 [Guillermo Calvo][AUTHOR].

[![][GUILLERMO_IMAGE]][GUILLERMO]


## License

This library is licensed under the *Apache License, Version 2.0* (the "License");
you may not use it except in compliance with the License.

You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License
is distributed on an "AS IS" BASIS, **WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND**, either express or implied.

See the License for the specific language governing permissions and limitations under the License.


**Permitted:**

- **Commercial Use**: You may use this library and derivatives for commercial purposes.
- **Modification**: You may modify this library.
- **Distribution**: You may distribute this library.
- **Patent Use**: This license provides an express grant of patent rights from contributors.
- **Private Use**: You may use and modify this library without distributing it.

**Required:**

- **License and Copyright Notice**: If you distribute this library you must include a copy of the license and copyright
  notice.
- **State Changes**: If you modify and distribute this library you must document changes made to this library.

**Forbidden:**

- **Trademark use**: This license does not grant any trademark rights.
- **Liability**: The library author cannot be held liable for damages.
- **Warranty**: This library is provided without any warranty.


[ARTIFACTS]:                    https://central.sonatype.com/artifact/com.leakyabstractions/result-lazy/
[AUTHOR]:                       https://github.com/guillermocalvo/
[CODE_OF_CONDUCT]:              https://github.com/LeakyAbstractions/.github/blob/main/CODE_OF_CONDUCT.md
[CONSUMER]:                     https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html
[CONTRIBUTING]:                 https://github.com/LeakyAbstractions/.github/blob/main/CONTRIBUTING.md
[GRADLE]:                       https://gradle.org/
[GUILLERMO]:                    https://guillermo.dev/
[GUILLERMO_IMAGE]:              https://guillermo.dev/assets/images/thumb.png
[JAVADOC]:                      https://javadoc.io/doc/com.leakyabstractions/result-lazy/
[LAZY_CONSUMER]:                https://javadoc.io/doc/com.leakyabstractions/result-lazy/latest/com/leakyabstractions/result/lazy/LazyConsumer.html
[LAZY_CONSUMER_OF]:             https://javadoc.io/doc/com.leakyabstractions/result-lazy/latest/com/leakyabstractions/result/lazy/LazyConsumer.html#of-java.util.function.Consumer-
[MAVEN]:                        https://maven.apache.org/
[OF_SUPPLIER]:                  https://javadoc.io/doc/com.leakyabstractions/result-lazy/latest/com/leakyabstractions/result/lazy/LazyResults.html#ofSupplier-java.util.function.Supplier-
[PRAGVER]:                      https://pragver.github.io/
[RESULT]:                       https://result.leakyabstractions.com/
[RESULT_FILTER]:                https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#filter-java.util.function.Predicate-java.util.function.Function-
[RESULT_FLAT_MAP]:              https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#flatMap-java.util.function.Function-java.util.function.Function-
[RESULT_FLAT_MAP_FAILURE]:      https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#flatMapFailure-java.util.function.Function-
[RESULT_FLAT_MAP_SUCCESS]:      https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#flatMapSuccess-java.util.function.Function-
[RESULT_GET_FAILURE]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#getFailure--
[RESULT_GET_SUCCESS]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#getSuccess--
[RESULT_HAS_FAILURE]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#hasFailure--
[RESULT_HAS_SUCCESS]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#hasSuccess--
[RESULT_IF_FAILURE]:            https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#ifFailure-java.util.function.Consumer-
[RESULT_IF_SUCCESS]:            https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#ifSuccess-java.util.function.Consumer-
[RESULT_IF_SUCCESS_OR_ELSE]:    https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#ifSuccessOrElse-java.util.function.Consumer-java.util.function.Consumer-
[RESULT_MAP]:                   https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#map-java.util.function.Function-java.util.function.Function-
[RESULT_MAP_FAILURE]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#mapFailure-java.util.function.Function-
[RESULT_MAP_SUCCESS]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#mapSuccess-java.util.function.Function-
[RESULT_OR_ELSE]:               https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#orElse-S-
[RESULT_OR_ELSE_MAP]:           https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#orElseMap-java.util.function.Function-
[RESULT_RECOVER]:               https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#recover-java.util.function.Predicate-java.util.function.Function-
[RESULT_STREAM_FAILURE]:        https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#streamFailure--
[RESULT_STREAM_SUCCESS]:        https://javadoc.io/doc/com.leakyabstractions/result-api/latest/com/leakyabstractions/result/api/Result.html#streamSuccess--
[SOURCE_CODE]:                  https://github.com/LeakyAbstractions/result-lazy/tree/main/result-lazy/src/test/java/example
[SUPPLIER]:                     https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html
[SUPPORT]:                      https://github.com/LeakyAbstractions/.github/blob/main/SUPPORT.md
