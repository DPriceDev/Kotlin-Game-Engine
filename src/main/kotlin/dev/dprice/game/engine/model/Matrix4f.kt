package dev.dprice.game.engine.model

class Matrix4f(values: List<Float> = identity) {
    private val values: List<Float> = values.let {
        when {
            it.size >= 16 -> it.take(16)
            else -> ((it.size + 16)..16).fold(it) { acc, index ->
                acc.plus(0f)
            }
        }
    }

    constructor(vararg values: Float) : this(values.toList())

    fun array() = values.toList()

    fun columns(): List<List<Float>> {
        val index = values.withIndex().map { it.index % 4 to it.value }
        val group = index.groupBy { it.first }
        val mapped = group.map {
            it.value.map { it.second }
        }
        return mapped
    }

    fun rows() = values.chunked(4)

    operator fun plus(other: Matrix4f) = Matrix4f(
        values.mapIndexed { index, value ->
            value + other.values[index]
        }
    )

    operator fun minus(other: Matrix4f) = Matrix4f(
        values.mapIndexed { index, value -> value - other.values[index] }
    )

    operator fun times(other: Matrix4f) = Matrix4f(
        rows().flatMap { row ->
            other.columns().map { column ->
                column.foldIndexed(0f) { index, acc, value ->
                    acc + (value * row[index])
                }
            }
        }
    )

    companion object {
        val identity = listOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        fun identity() = Matrix4f(identity)
    }
}

operator fun Matrix4f.times(value: Float) = Matrix4f(
    array().map { it * value }
)

operator fun Matrix4f.times(value: Vector4f) = Matrix4f(
    array().chunked(4)
        .flatMapIndexed { rowIndex, row ->
            row.map { it * value[rowIndex] }
        }
)

fun Matrix4f.asFloatArray() = columns().flatten().toFloatArray()

fun Matrix4f.translate(translation: Vector3f) = this + Matrix4f(
    0f, 0f, 0f, translation.x,
    0f, 0f, 0f, translation.y,
    0f, 0f, 0f, translation.z,
    0f, 0f, 0f, 0f
)

fun Matrix4f.scale(scale: Vector3f) = this * Matrix4f(
    scale.x, 0f, 0f, 0f,
    0f, scale.y, 0f, 0f,
    0f, 0f, scale.z, 0f,
    0f, 0f, 0f, 1f
)