# kotlin-either

Functional Error/Success Handling

Provides an algebraic data type which is **either** a **failure** or a **success**.

Both `Success` and `Failure` classes extend sealed class `Either` so they can be type checked exhaustively in Kotlin expressions without the need for an `else` branch:

```
val result = 
    when(either) {
        is Failure -> ...
        is Success -> ...
    }
```

`Success` and `Failure` are wrappers which can wrap any type, like e.g. a response from an HTTP request or an HTTP error. Exceptions can of course be wrapped in `Failure` as well.

In Kotlin this provides an advantage over `try/catch` blocks in that it requires the `Either` consumer to actually handle the error case, as in Kotlin there are no checked exceptions.
This is because exceptions should be used for exceptional situations, which cannot be predicted while writing the program, like programmer errors. In this case the application should crash early and crash hard so that the programmer can find the issue early and fix it.
Errors like HTTP 404, which **SHOULD** be handled in the application and not crash, error types should be used. `Either` is perfect for this.

## API
You can `flatMap` as many functions returning `Either` type as you wish. The `flatMap` function will always take the result of the previous function, unwrap it if it's a `Success` and pass it as an argument to the next function.

If any of the flatmapped functions returns a `Failure`, the first `Failure` will be returned to the caller immediately and none of the following functions will be called. This let's you interrupt the call chain if at any of the stages something goes wrong.

```kotlin
connectToServer()
    .flatMap { getUserId() }
    .flatMap { id -> getUserProfile(id) }
    .flatMap { profile -> saveProfileToDatabase(profile) }
```

There is also a `fold` function which can be used to handle the end result of a function (or a function chain) returning an `Either`. You can provide lambdas for success and failure cases.

```kotlin
getUserData().fold(
    failed = { failure -> showError(failure) },
    succeeded = { success -> showUserData(success) }
)
```

The `map {}` function can be used to map the **success** value to another value. 
Imagine we get a `Either<User>` but only want to return the `id` of that user:


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

## Usage
The library is available on the gcx GitHub Packages Maven instance.

In your project main `build.gradle.kts` you need:

```
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/grandcentrix/grandcentrix-kotlin-either")
    }
}
```

And in your module `build.gradle.kts`:

```
implementation("net.grandcentrix.either:either:1.3")
```

For the newest version please always check the [Releases](https://github.com/grandcentrix/grandcentrix-kotlin-either/releases) tab.
