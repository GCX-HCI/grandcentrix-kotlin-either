package net.grandcentrix.either.calladapter

/**
 * Error hierarchy when calling remote server.
 */
sealed class CallError(open val throwable: Throwable? = null)

/**
 * Http request returned an error response.
 */
data class HttpError(val code: Int, val body: String) : CallError(Exception(body))

/**
 * The request timed out.
 */
data class TimeoutError(override val throwable: Throwable) : CallError(throwable)

/**
 * Network error on client.
 */
data class NetworkError(override val throwable: Throwable) : CallError(throwable)

/**
 * Unknown API error.
 */
data class UnexpectedCallError(override val throwable: Throwable = Exception("Unknown Call error")) :
    CallError(throwable) {

    constructor(message: String) : this(Exception(message))
}
