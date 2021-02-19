package net.grandcentrix.either.calladapter

/**
 * Error hierarchy when calling remote server.
 */
sealed class CallError(open val cause: Throwable?)

/**
 * Http request returned an error response.
 */
data class HttpError(val code: Int, val body: String, override val cause: Throwable? = null) : CallError(cause)

/**
 * The request timed out.
 */
data class TimeoutError(override val cause: Throwable? = null) : CallError(cause)

/**
 * Network error on client.
 */
data class NetworkError(override val cause: Throwable) : CallError(cause)

/**
 * Unknown API error.
 */
data class UnexpectedCallError(override val cause: Throwable) : CallError(cause) {

    constructor(message: String) : this(Exception(message))
}
