package dev.dprice.game.engine.util

class SparseArray<T: Any>(
    private var elements: Array<Any>,
    private var indices: Array<Int> = emptyArray()
) : Collection<T> {
    override val size: Int = elements.size

    override fun isEmpty(): Boolean = elements.isEmpty()

    @Suppress("UNCHECKED_CAST")
    override fun iterator() = (elements as Array<T>).iterator()

    override fun containsAll(elements: Collection<T>) = this.elements.all { contains(it) }

    override fun contains(element: T) = elements.contains(element)

    fun getOrNull(index: Int) : T? {
        return indices
            .indexOf(index)
            .let { elements.getOrNull(it) as T? }
    }

    fun <T> add(index: Int, item: T) {
        insertMapping(index)
        elements = elements.toMutableList().apply {
            val mappedIndex = this@SparseArray.indices.indexOf(index)
            this.add(mappedIndex, item as Any)
        }.toTypedArray()
    }

    fun remove(index: Int) {
        elements = elements.toMutableList().apply {
            val mappedIndex = this@SparseArray.indices.indexOf(index)
            removeAt(mappedIndex)
        }.toTypedArray()

        removeMapping(index)
    }

    private fun insertMapping(index: Int) {
        indices = indices
            .toMutableList()
            .apply { add(index) }
            .toTypedArray()
            //.convertListToRanges()
    }

    private fun removeMapping(index: Int) {
        indices = indices
            .toMutableList()
            .apply { remove(index) }
            .toTypedArray()
    }

//    private fun List<Int>.convertListToRanges() : List<IntRange> {
//        return sorted()
//            .distinct()
//            .fold(listOf()) { ranges, index ->
//                val last = ranges.lastOrNull()
//                if (last != null) {
//
//                    if (last.last + 1 != index) {
//                        ranges.plusElement(index..index)
//                    } else {
//                        ranges.toMutableList()
//                            .dropLast(1)
//                            .plusElement(ranges.last().first..index)
//                    }
//                } else {
//                    ranges.plusElement(index..index)
//                }
//            }
//    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Array<T>.asSparseArray(): SparseArray<T> = SparseArray(this as Array<Any>)

fun <T: Any> Map<out Int, T>.asSparseArray(): SparseArray<T> = SparseArray(
    values.toTypedArray(),
    keys.toTypedArray()
)

fun <T: Any> sparseArrayOf(vararg elements: T): SparseArray<T> = if (elements.isNotEmpty()) elements.asSparseArray() as SparseArray<T> else emptySparseArray()

fun <T: Any> sparseArrayOf(): SparseArray<T> = emptySparseArray()

fun <T: Any> emptySparseArray(): SparseArray<T> = SparseArray(emptyArray())
