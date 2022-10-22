package dev.dprice.game.engine.model

import kotlin.math.cos
import kotlin.math.sin

class Matrix4f(private val values: Array<Float> = identity) {
    constructor(vararg values: Float) : this(values.toTypedArray())
    init {
        require(values.size == 16)
    }

    fun array() = values

    fun columns(): List<List<Float>> {
        val index = values.withIndex().map { it.index % 4 to it.value }
        val group = index.groupBy { it.first }
        val mapped = group.map {
            it.value.map { it.second }
        }
        return mapped
    }

    fun rows() = values.toList().chunked(4)

    operator fun plus(other: Matrix4f) = Matrix4f(
        values.mapIndexedTo(arrayListOf()) { index, value ->
            value + other.values[index]
        }.toTypedArray()
    )

    operator fun minus(other: Matrix4f) = Matrix4f(
        values.mapIndexed { index, value -> value - other.values[index] }.toTypedArray()
    )

    operator fun times(other: Matrix4f) = Matrix4f(
        rows().flatMap { row ->
            other.columns().map { column ->
                column.foldIndexed(0f) { index, acc, value ->
                    acc + (value * row[index])
                }
            }
        }.toTypedArray()
    )

    companion object {
        val identity = arrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        fun identity() = Matrix4f(identity)
    }
}

operator fun Matrix4f.times(value: Float) = Matrix4f(
    array().map { it * value }.toTypedArray()
)

operator fun Matrix4f.times(value: Vector4f) = Matrix4f(
    array().toList().chunked(4)
        .flatMapIndexed { rowIndex, row ->
            row.map { it * value[rowIndex] }
        }
        .toTypedArray()
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

fun Matrix4f.rotate(rotation: Degree, axis: Vector3f) : Matrix4f {
    val cos = cos(rotation.toRadians().value)
    val cosMinus = (1 - cos)
    val sin = sin(rotation.toRadians().value)

    return with(axis.normalize()) {
        val aa = x * x * cosMinus + cos
        val ab = x * y * cosMinus - z * sin
        val ac = x * z * cosMinus + y * sin
        val ba = y * x * cosMinus + z * sin
        val bb = y * y * cosMinus + cos
        val bc = y * z * cosMinus - x * sin
        val ca = z * x * cosMinus - y * sin
        val cb = z * y * cosMinus + x * sin
        val cc = z * z * cosMinus + cos

        this@rotate * Matrix4f(
            aa, ab, ac, 0f,
            ba, bb, bc, 0f,
            ca, cb, cc, 0f,
            0f, 0f, 0f, 1f
        )
    }
}

fun Matrix4f.rotate(rotation3f: Rotation3f) : Matrix4f {
    return rotate(rotation3f.pitch, Vector3f(x = 1f))
        .rotate(rotation3f.roll, Vector3f(y = 1f))
        .rotate(rotation3f.yaw, Vector3f(z = 1f))
}