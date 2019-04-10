package net.grandcentrix.either

import org.junit.jupiter.api.Test

class EitherTest {

    @Test
    fun Failure() {
        val either: Either<Int, Int> = Failure(123)

        val result = when (either) {
            is Failure  -> either.failure * 2
            is Success -> -1
        }

        check(result == 123 * 2)
    }

    @Test
    fun Success() {
        val either: Either<Int, String> = Success("Success")

        val result = when (either) {
            is Failure  -> "${either.failure}Failure"
            is Success -> "${either.success}Success"
        }

        check(result == "SuccessSuccess")
    }
}