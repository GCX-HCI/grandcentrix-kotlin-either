package net.grandcentrix.either

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


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
}