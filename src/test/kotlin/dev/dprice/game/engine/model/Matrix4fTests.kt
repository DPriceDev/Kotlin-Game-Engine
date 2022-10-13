package dev.dprice.game.engine.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Matrix4fTests {

    @Test
    fun `given we have two matrices - when we multiply them - the result is correct`() {
        val matrixA = Matrix4f(
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 2f,
            0f, 0f, 1f, 3f,
            0f, 0f, 0f, 1f
        )

        val matrixB = Matrix4f(
            2f, 0f, 0f, 0f,
            0f, 2f, 0f, 0f,
            0f, 0f, 2f, 0f,
            0f, 0f, 0f, 1f
        )

        val result = matrixA * matrixB

        assertEquals(
            Matrix4f(
                2f, 0f, 0f, 1f,
                0f, 2f, 0f, 2f,
                0f, 0f, 2f, 3f,
                0f, 0f, 0f, 1f
            ).array(),
            result.array()
        )
    }

    @Test
    fun `given a matrix - when we get the columns - the columns are correct`() {
        val matrix = Matrix4f(
            1f, 5f, 9f, 13f,
            2f, 6f, 10f, 14f,
            3f, 7f, 11f, 15f,
            4f, 8f, 12f, 16f
        )

        val columns = matrix.columns()

        assertEquals(
            listOf(
                listOf(1f, 2f, 3f, 4f),
                listOf(5f, 6f, 7f, 8f),
                listOf(9f, 10f, 11f, 12f),
                listOf(13f, 14f, 15f, 16f)
            ),
            columns
        )
    }

    @Test
    fun `given a matrix - when we get the rows - the rows are correct`() {
        val matrix = Matrix4f(
            1f, 2f, 3f, 4f,
            5f, 6f, 7f, 8f,
            9f, 10f, 11f, 12f,
            13f, 14f, 15f, 16f
        )

        val rows = matrix.rows()

        assertEquals(
            listOf(
                listOf(1f, 2f, 3f, 4f),
                listOf(5f, 6f, 7f, 8f),
                listOf(9f, 10f, 11f, 12f),
                listOf(13f, 14f, 15f, 16f)
            ),
            rows
        )
    }
}