package net.grandcentrix.either

import org.junit.jupiter.api.Test


class EitherTest {

    @Test
    fun `test flatMap returns mapped value`() {
        val eitherOriginal: Either<Int, String> = Success("original result")
        val eitherMapTo: Either<Int, String> = Success("map to result")

        val mappedEither = eitherOriginal.flatMap { eitherMapTo }

        val result = mappedEither as Success
        check(result.success == "map to result")
    }
}