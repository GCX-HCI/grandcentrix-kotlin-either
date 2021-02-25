package net.grandcentrix.either

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class EitherTest {

    @Test
    fun `test that flatMap returns mapped value on success`() {
        val eitherOriginal: Either<Int, String> = Success("original result")
        val eitherMapTo: Either<Int, String> = Success("map to result")

        val mappedEither = eitherOriginal.flatMap { eitherMapTo }

        assertThat(mappedEither).isInstanceOf(Success::class.java)

        val result = mappedEither as Success
        assertThat(result.success).isEqualTo("map to result")
    }

    @Test
    fun `test that flatMap returns mapped failure when success mapped to failure`() {
        val eitherOriginal: Either<Int, String> = Success("original result")
        val eitherMapTo: Either<Int, String> = Failure(42)

        val mappedEither = eitherOriginal.flatMap { eitherMapTo }

        assertThat(mappedEither).isInstanceOf(Failure::class.java)

        val result = mappedEither as Failure
        assertThat(result.failure).isEqualTo(42)
    }

    @Test
    fun `test that flatMap returns original failure when failure mapped to another failure`() {
        val eitherOriginal: Either<Int, String> = Failure(2)
        val eitherMapTo: Either<Int, String> = Failure(42)

        val mappedEither = eitherOriginal.flatMap { eitherMapTo }

        assertThat(mappedEither).isInstanceOf(Failure::class.java)

        val result = mappedEither as Failure
        assertThat(result.failure).isEqualTo(2)
    }

    @Test
    fun `test that fold returns value on success`() {
        val either: Either<Int, String> = Success("success result")

        var result: String? = null
        either.fold(
            failed = {
                //ignore
            },
            succeeded = {
                result = it
            }
        )

        assertThat(result).isEqualTo("success result")
    }

    @Test
    fun `test that fold returns error on failure`() {
        val either: Either<Exception, String> = Failure(IllegalStateException())

        var result: Exception? = null
        either.fold(
            failed = {
                result = it
            },
            succeeded = {
                //ignore
            }
        )

        assertThat(result).isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `test that map returns mapped value on success`() {
        val eitherOriginal: Either<Int, String> = Success("original result")

        val mappedEither = eitherOriginal.map { true }

        assertThat(mappedEither).isInstanceOf(Success::class.java)

        val result = mappedEither as Success
        assertThat(result.success).isTrue()
    }

    @Test
    fun `test that map returns failure on failed`() {
        val eitherOriginal: Either<String, String> = Failure("Failure")

        val mappedEither = eitherOriginal.map { true }

        assertThat(mappedEither).isInstanceOf(Failure::class.java)

        val result = mappedEither as Failure
        assertThat(result.failure).isEqualTo("Failure")
    }

    @Test
    fun `test that mapFailure returns unchanged value on success`() {
        val eitherOriginal: Either<Int, String> = Success("original result")

        val mappedEither = eitherOriginal.mapFailure { true }

        assertThat(mappedEither).isInstanceOf(Success::class.java)

        val result = mappedEither as Success
        assertThat(result.success).isEqualTo("original result")
    }

    @Test
    fun `test that mapFailure returns mapped failure on failed`() {
        val eitherOriginal: Either<String, String> = Failure("Failure")

        val mappedEither = eitherOriginal.mapFailure { true }

        assertThat(mappedEither).isInstanceOf(Failure::class.java)

        val result = mappedEither as Failure
        assertThat(result.failure).isTrue()
    }

    @Test
    fun `test that onSuccess is called on success`() {
        val either: Either<Int, String> = Success("success result")

        var result: String? = null
        either.onSuccess { result = it }

        assertThat(result).isEqualTo("success result")
    }

    @Test
    fun `test that onFailure is not called on success`() {
        Success("success result")
            .onFailure { fail { "Should not be called" } }
    }

    @Test
    fun `test that onFailure is called on failure`() {
        val either: Either<String, String> = Failure("failure result")

        var result: String? = null
        either.onFailure { result = it }

        assertThat(result).isEqualTo("failure result")
    }

    @Test
    fun `test that onSuccess is not called on failure`() {
        Failure("failure result")
            .onSuccess { fail { "Should not be called" } }
    }

    @Test
    fun `test that successOrNull return null if failure`() {
        val successOrNull = Failure("failure result").successOrNull
        assertNull(successOrNull)
    }

    @Test
    fun `test that failureOrNull return null if success`() {
        val failureOrNull = Success("success result").failureOrNull
        assertNull(failureOrNull)
    }

    @Test
    fun `when catch with no exception return success`() {
        val result = Either.catch { "success result" }
        assertThat(result).isEqualTo(Success("success result"))
    }

    @Test
    fun `when catch with exception return failure with exception`() {
        val exception = IllegalStateException()
        val result = Either.catch { throw exception }
        assertThat(result).isEqualTo(Failure(exception))
    }

    @Test
    fun `when success then isSuccess returns true`() {
        assertTrue(Success("success result").isSuccess)
    }

    @Test
    fun `when failure then isSuccess returns false`() {
        assertFalse(Failure("failure result").isSuccess)
    }

    @Test
    fun `when success then isFailure returns false`() {
        assertFalse(Success("success result").isFailure)
    }

    @Test
    fun `when failure then isFailure returns true`() {
        assertTrue(Failure("failure result").isFailure)
    }

    @Test
    fun `when success then getOrElse returns success value`() {
        val either: Either<Throwable, String> = Success("success")
        val result = either.getOrElse { "else" }
        assertThat(result).isEqualTo("success")
    }

    @Test
    fun `when failure then getOrElse returns else block`() {
        val either: Either<String, String> = Failure("failure result")
        val result = either.getOrElse { "else" }
        assertThat(result).isEqualTo("else")
    }
}