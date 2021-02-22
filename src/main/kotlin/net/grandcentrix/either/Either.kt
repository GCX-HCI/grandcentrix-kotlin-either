package net.grandcentrix.either


/**
 * An algebraic data type to provide either a [Failure][F] or a [Success][S] result.
 */
sealed class Either<out F, out S> {

    /**
     * Calls [failed] with the [failure][Failure.failure] value if result is a [Failure]
     * or [succeeded] with the [success][Success.success] value if result is a [Success]
     */
    inline fun <T> fold(failed: (F) -> T, succeeded: (S) -> T): T =
        when (this) {
            is Failure -> failed(failure)
            is Success -> succeeded(success)
        }
}

data class Failure<out F>(val failure: F) : Either<F, Nothing>()

data class Success<out S>(val success: S) : Either<Nothing, S>()

/**
 * Allows chaining of multiple calls taking as argument the [success][Success.success] value of the previous call and
 * returning an [Either].
 *
 * 1. Unwrap the result of the first call from the [Either] wrapper.
 * 2. Check if it is a [Success].
 * 3. If yes, call the next function (passed as [ifSucceeded]) with the value of the [success][Success.success]
 * property as an input parameter (chain the calls).
 * 4. If no, just pass the [Failure] through as the end result of the whole call chain.
 *
 * In case any of the calls in the chain returns a [Failure], none of the subsequent flatmapped functions is called
 * and the whole chain returns this failure.
 *
 * @param ifSucceeded next function which should be called if this is a [Success]. The [success][Success.success]
 * value will be then passed as the input parameter.
 */
inline fun <F, S1, S2> Either<F, S1>.flatMap(succeeded: (S1) -> Either<F, S2>): Either<F, S2> =
    fold({ this as Failure }, succeeded)

/**
 * Map the [Success] value of the [Either] to another value.
 *
 * You can for example map a `Success<String>` to a `Success<Int>` by
 * using the following code:
 * ```
 * val fiveString: Either<Nothing, String> = Success("5")
 * val fiveInt: Either<Nothing, Int> = fiveString.map { it.toInt() }
 * ```
 */
inline fun <F, S1, S2> Either<F, S1>.map(f: (S1) -> S2): Either<F, S2> =
    flatMap { Success(f(it)) }

/**
 * Map the [Failure] value of the [Either] to another value. It will leave the [Success] value unchanged.
 *
 * You can for example map a `Failure<Throwable>` to a `Failure<String>` by
 * using the following code:
 * ```
 * val failure: Either<Throwable, Nothing> = Failure(Exception("Some error happened"))
 * val failureMessage: Either<String, Nothing> = failure.mapFailure { it.message }
 * ```
 */
inline fun <F1, F2, S> Either<F1, S>.mapFailure(f: (F1) -> F2): Either<F2, S> =
    fold({ Failure(f(it)) }, { Success(it) })

/**
 * Return the [Success] value of the [Either] if exist. If no success value exist it will return null.
 */
val <S> Either<*, S>.successOrNull: S?
    get() = (this as? Success<S>)?.success

/**
 * Return the [Failure] value of the [Either] if exist. If no failure value exist it will return null.
 */
val <F> Either<F, *>.failureOrNull: F?
    get() = (this as? Failure<F>)?.failure

/**
 * Executes the given code [block] when [Either] is [Success].
 * @return It will leave the original [Either] unchanged.
 */
inline fun <F, S> Either<F, S>.onSuccess(block: (success: S) -> Unit): Either<F, S> = also {
    if (it is Success<S>) block(it.success)
}

/**
 * Executes the given code [block] when [Either] is [Failure].
 * @return It will leave the original [Either] unchanged.
 */
inline fun <F, S> Either<F, S>.onFailure(block: (failure: F) -> Unit): Either<F, S> = also {
    if (it is Failure<F>) block(it.failure)
}

/**
 * Executes the given code [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
inline fun <T, R> T.catchEither(block: T.() -> R): Either<Throwable, R> = try {
    Success(block())
} catch (e: Throwable) {
    Failure(e)
}

/**
 * Executes the given suspend code [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
suspend inline fun <R> suspendEither(block: suspend () -> R): Either<Throwable, R> = try {
    Success(block())
} catch (e: Throwable) {
    Failure(e)
}