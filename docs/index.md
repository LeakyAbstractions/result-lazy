---
title: Lazy Resuls
description: Result-Lazy provides lazy versions of Result objects
image: /result-banner-centered.png
---

# Lazy Resuls

This library provides lazy versions of
[Result objects](https://dev.leakyabstractions.com/result/).


## Adding Assertions to Your Build

The library requires JDK 1.8 or higher. Other than that, it has no external dependencies and it is very lightweight.
Adding it to your build should be very easy.

Artifact coordinates:

- Group ID: `com.leakyabstractions`
- Artifact ID: `result-lazy`
- Version: `{{ site.current_version }}`

To add the dependency using [**Maven**](https://maven.apache.org/), use the following:

```xml
<dependency>
    <groupId>com.leakyabstractions</groupId>
    <artifactId>result-lazy</artifactId>
    <version>{{ site.current_version }}</version>
    <scope>test</scope>
</dependency>
```

To add the dependency using [**Gradle**](https://gradle.org/), if you are building an application that will use
<tt>Result</tt> internally:

```gradle
dependencies {
    implementation 'com.leakyabstractions:result-lazy:{{ site.current_version }}'
}
```

If you are building a library that will use <tt>Result</tt> type in its public API, you should use instead:

```gradle
dependencies {
    api 'com.leakyabstractions:result-lazy:{{ site.current_version }}'
}
```
## Lazy Results

_Lazy_ results encapsulate expensive operations that can be entirely omitted (as an optimization). These result can be
manipulated just like any other, but the encapsulated operation will not be executed unless there's an actual check for
success/failure.

To create a _lazy_ result we need to use static method [`Results.lazy()`][LAZY]:

```java
    Result<String, Void> expensiveCalculation(AtomicLong timesExecuted) {
        timesExecuted.getAndIncrement();
        return Results.success("HELLO");
    }

    @Test
    void should_not_execute_expensive_action() {
        final AtomicLong timesExecuted = new AtomicLong();
        // Given
        final Result<String, Void> lazy = Results
                .lazy(() -> this.expensiveCalculation(timesExecuted));
        // When
        final Result<Integer, Void> transformed = lazy.mapSuccess(String::length);
        // Then
        assertThat(transformed).isNotNull();
        assertThat(timesExecuted).hasValue(0);
    }
```

Lazy results can be manipulated just like any other result; they will try to defer the invocation of the given supplier
as long as possible. For example, when we actually try to determine if the operation succeeded or failed.

```java
    @Test
    void should_execute_expensive_action() {
        final AtomicLong timesExecuted = new AtomicLong();
        // Given
        final Result<String, Void> lazy = Results
                .lazy(() -> this.expensiveCalculation(timesExecuted));
        // When
        final Result<Integer, Void> transformed = lazy.mapSuccess(String::length);
        final boolean success = transformed.isSuccess();
        // Then
        assertThat(success).isTrue();
        assertThat(timesExecuted).hasValue(1);
    }
```


## Releases

This library adheres to [Pragmatic Versioning](https://pragver.github.io/).

Artifacts are available in [Maven Central](https://search.maven.org/artifact/com.leakyabstractions/result-assertj).


## Javadoc

Here's the full
[Result API documentation](https://dev.leakyabstractions.com/result-assertj/javadoc/{{ site.current_version }}/).


## Looking for Support?

We'd love to help. Check out the [support guidelines](SUPPORT.md).


## Contributions Welcome

If you'd like to contribute to this project, please [start here](CONTRIBUTING.md).


## Code of Conduct

This project is governed by the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are
expected to uphold this code.

[LAZY]: https://dev.leakyabstractions.com/result-lazy/javadoc/{{ site.current_version }}/com/leakyabstractions/result/Results.html#lazy-java.util.function.Supplier-
