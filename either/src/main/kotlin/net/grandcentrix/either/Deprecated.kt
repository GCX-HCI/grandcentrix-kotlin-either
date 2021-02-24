package net.grandcentrix.either

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
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    "Use the class method instead of extension function (simply remove import net.grandcentrix.either.map)"
)
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
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    "Use the class method instead of extension function (simply remove import net.grandcentrix.either.mapFailure)"
)
inline fun <F1, F2, S> Either<F1, S>.mapFailure(f: (F1) -> F2): Either<F2, S> =
    fold({ Failure(f(it)) }, { Success(it) })

/**
 * Return the [Success] value of the [Either] if exist. If no success value exist it will return null.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    "Use the class property instead of extension property (simply remove import net.grandcentrix.either.successOrNull)"
)
val <S> Either<*, S>.successOrNull: S?
    get() = (this as? Success<S>)?.success

/**
 * Return the [Failure] value of the [Either] if exist. If no failure value exist it will return null.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    "Use the class property instead of extension property (simply remove import net.grandcentrix.either.failureOrNull)"
)
val <F> Either<F, *>.failureOrNull: F?
    get() = (this as? Failure<F>)?.failure

/**
 * Executes the given code [block] when [Either] is [Success].
 * @return It will leave the original [Either] unchanged.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    "Use the class method instead of extension function (simply remove import net.grandcentrix.either.onSuccess)"
)
inline fun <F, S> Either<F, S>.onSuccess(block: (success: S) -> Unit): Either<F, S> = also {
    if (it is Success<S>) block(it.success)
}

/**
 * Executes the given code [block] when [Either] is [Failure].
 * @return It will leave the original [Either] unchanged.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "DeprecatedCallableAddReplaceWith")
@Deprecated(
    "Use the class method instead of extension function (simply remove import net.grandcentrix.either.onFailure)"
)
inline fun <F, S> Either<F, S>.onFailure(block: (failure: F) -> Unit): Either<F, S> = also {
    if (it is Failure<F>) block(it.failure)
}
