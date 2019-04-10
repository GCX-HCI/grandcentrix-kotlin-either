package net.grandcentrix.either

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.lang.IllegalStateException


class EitherTest {

    @Test
    fun `test that flatMap returns mapped value`() {
        val eitherOriginal: Either<Int, String> = Success("original result")
        val eitherMapTo: Either<Int, String> = Success("map to result")

        val mappedEither = eitherOriginal.flatMap { eitherMapTo }

        val result = mappedEither as Success
        assertThat(result.success).isEqualTo("map to result")
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
    fun `test that fold returns error on success`() {
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
}