# kotlin-either

![Publish Artifact](https://github.com/grandcentrix/grandcentrix-kotlin-either/workflows/Publish%20Artifact/badge.svg)

## Functional Error/Success Handling

Provides an algebraic data type which is **either** a **failure** or a **success**. 
It is implemented as a `sealed class Either` with two child classes: `Success` and `Failure`.

`Success` and `Failure` are wrappers which can wrap any type, like e.g. a response from an HTTP request or an HTTP error. Exceptions can of course be wrapped in `Failure` as well.

In Kotlin this provides an advantage over `try/catch` blocks in that it requires the `Either` consumer to actually handle the error case, as in Kotlin there are no checked exceptions.
This is because exceptions should be used for exceptional situations, which cannot be predicted while writing the program, like programmer errors. In this case the application should crash early and crash hard so that the programmer can find the issue early and fix it.
For domain errors like HTTP 404, which **SHOULD** be handled in the application and not crash, error types should be used. `Either` is perfect for this.

## API

To handle the end result of a function (or a function chain) returning an `Either` use `fold()`. You can provide lambdas for success and failure cases:

```kotlin
getUserData().fold(
    failed = { failure -> showError(failure) },
    succeeded = { success -> showUserData(success) }
)
```

To chain multiple functions, you can use `flatMap {}`. The `flatMap` function will always take the result of the previous function, unwrap it if it's a `Success` and pass it as an argument to the next function.

If any of the flatmapped functions returns a `Failure`, the first `Failure` will be returned to the caller immediately and none of the following functions will be called. This let's you interrupt the call chain if at any of the stages something goes wrong.

```kotlin
connectToServer()
    .flatMap { getUserId() }
    .flatMap { id -> getUserProfile(id) }
    .flatMap { profile -> saveProfileToDatabase(profile) }
```

The `map {}` function can be used to map the **success** value to another value. 
Imagine we get an `Either<User>` but only want to return the `id` of that user:


```kotlin
getUserData().map { it.id }
```

There is also a `mapFailure {}` function which is the opposite of `map`. It simply maps the `Failure` result using
provided lambda. In case of `Success` it just passes through the success value untouched.

It can be useful if you want to take some action based on the type of failure you received or if you want to map
a domain type exception to a public api exception:

```kotlin
getUserDataOrFail().mapFailure {
    when(it) {
        is AuthorizationException -> authorizeUser()
        is TimeoutException -> GetUserDataFailedException("Timeout")
        else -> it // just pass through 
    }
}
```

As all functions are `inline` they can be used with suspending functions as well. So you can `flatMap` suspending with non-suspending functions in one chain.

Also the following helpers exist:
```kotlin
successOrNull // property returning the success value if Success or null if Failure
failureOrNull // property returning the failure value if Failure or null if Success
onSuccess {} // executes given code block as side effect if Success and returns passed Either value unchanged
onFailure {} // executes given code block as side effect if Failure and returns passed Either value unchanged
Either.catch {} // executes the given code and returns its encapsulated result if invocation was successful and catching any exception that was thrown as a failure
```

## Additional Utilities

### `EitherCallAdapter`

A call adapter for easy use of the `Either` type as result of a Retrofit interface function.

#### Usage

When attaching the `EitherCallAdapter` to the Retrofit interface instantiation like

```
Retrofit.Builder()
    .baseUrl(<Base Url>)
    …
    .addCallAdapterFactory(EitherCallAdapterFactory())
    .build()
    .create(<Retrofit Interface>::class.java)
```

you can use the `Either` type as a return type of a Retrofit interface function

```
interface Api {
    
    @GET
    suspend fun get(@Url url: String): Either<CallError, String>
}
```

## Inclusion to your Project
The library is available on GitHub Packages.

In your project main `build.gradle.kts` you need:

```
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/grandcentrix/grandcentrix-kotlin-either")
    }
}
```

And in your module `build.gradle.kts`:

```
implementation("net.grandcentrix.either:either:<version>")
implementation("net.grandcentrix.either:retrofit-calladapter:<version>")
```

For the newest version please always check the [Packages](https://github.com/grandcentrix/grandcentrix-kotlin-either/packages/596752) page.
The version should be the same for all artifacts to ensure their compatibility.
