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

```
connectToServer()
    .flatMap { getUserId() }
    .flatMap { id -> getUserProfile(id) }
    .flatMap { profile -> saveProfileToDatabase(profile) }
```

There is also a `fold` function which can be used to handle the end result of a function (or a function chain) returning an `Either`. You can provide lambdas for success and failure cases.

```
getUserData().fold(
    failed = { failure -> showError(failure) }
    succeeded = { success -> showUserData(success) }
)
```

As both `fold` and `flatMap` functions are `inline` they can be used with suspending functions as well. So you can `flatMap` suspending with non-suspending functions in one chain.

## Usage
The library is available in our internal artifactory.

In your project main `build.gradle.kts` you need:

```
maven("https://artifactory.gcxi.de/maven-internal") {
    content { includeModule("net.grandcentrix.either", "kotlin-either") }
}
```

And in your module `build.gradle.kts`:

```
implementation("net.grandcentrix.either:kotlin-either:1.1")
```

For the newest version please always check the "Releases" tab.
