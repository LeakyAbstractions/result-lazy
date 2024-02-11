---
title: Lazy Results
description: Result-Lazy provides lazy versions of Result objects
image: https://dev.leakyabstractions.com/result/result-magic-ball.png
---

# Lazy Results

This library provides lazy versions of [Result objects][RESULT].

_Lazy_ results encapsulate expensive operations that can be entirely omitted (as an optimization). These result can be
manipulated just like any other, but the encapsulated operation will not be executed unless there's an actual check for
success/failure.


## Adding Lazy Results to Your Build

Artifact coordinates:

- Group ID: `com.leakyabstractions`
- Artifact ID: `result-lazy`
- Version: `{{ site.current_version }}`

[Maven Central Repository](https://central.sonatype.com/artifact/com.leakyabstractions/result-lazy/{{ site.current_version }})
provides snippets for different build tools to declare this dependency.


## Creating Lazy Results

To create a lazy result, all we need to do is invoke static method [`LazyResults.ofSupplier()`][OF_SUPPLIER]:

```java
{% include_relative result-lazy/src/test/java/example/Fragments.java fragment="creation" %}
```

As you can see, a supplier can simply return a fixed result. However, lazy results are more useful when they encapsulate
an actual method that either takes a long time to execute or potentially uses up scarce resources.

```java
{% include_relative result-lazy/src/test/java/example/Fragments.java fragment="expensive_calculation" %}
```

The good thing about lazy results is that they will try to defer the invocation of the given supplier as long as
possible. You can manipulate them just like any other result, though.

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_not_execute_expensive_action" %}
```

The previous test proves that a lazy result could be transformed and it kept behaving as a lazy result, which means that
the expensive calculation was never executed.

On the other hand, when it comes the time to evaluate whether the operation was actually successful or failed, the lazy
result will end up invoking the method.

```java
{% include_relative result-lazy/src/test/java/example/Example_Test.java test="should_execute_expensive_action" %}
```


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

Copyright 2024 [Guillermo Calvo][AUTHOR].

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


[ARTIFACTS]:                    https://search.maven.org/artifact/com.leakyabstractions/result-lazy/
[AUTHOR]:                       https://github.com/guillermocalvo/
[CODE_OF_CONDUCT]:              https://dev.leakyabstractions.com/result/CODE_OF_CONDUCT.html
[CONTRIBUTING]:                 https://dev.leakyabstractions.com/result/CONTRIBUTING.html
[GRADLE]:                       https://gradle.org/
[GUILLERMO]:                    https://guillermo.dev/
[GUILLERMO_IMAGE]:              https://guillermo.dev/assets/images/thumb.png
[JAVADOC]:                      https://javadoc.io/doc/com.leakyabstractions/result-lazy/
[MAVEN]:                        https://maven.apache.org/
[OF_SUPPLIER]:                  https://javadoc.io/static/com.leakyabstractions/result-lazy/{{ site.current_version }}/com/leakyabstractions/result/lazy/LazyResults.html#ofSupplier-java.util.function.Supplier-
[PRAGVER]:                      https://pragver.github.io/
[RESULT]:                       https://dev.leakyabstractions.com/result/
[SUPPORT]:                      https://dev.leakyabstractions.com/result/SUPPORT.html
