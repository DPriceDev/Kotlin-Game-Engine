package util

class SparseArray<T> : Collection<T> {
    private var mappings: List<IntRange> = emptyList()
    private var contents: List<T> = emptyList()

    override val size: Int = contents.size

    override fun isEmpty(): Boolean = contents.isEmpty()

    override fun iterator() = contents.iterator()

    override fun containsAll(elements: Collection<T>) = contents.containsAll(elements)

    override fun contains(element: T) = contents.contains(element)

    fun getOrNull(index: Int) : T? {
        return mappings.flatten()
            .indexOf(index)
            .let { contents.getOrNull(it) }
    }

    fun add(index: Int, item: T) {
        insertMapping(index)
        contents = contents.toMutableList().apply {
            val mappedIndex = mappings.flatten().indexOf(index)
            add(mappedIndex, item)
        }
    }

    fun remove(index: Int) {
        contents = contents.toMutableList().apply {
            val mappedIndex = mappings.flatten().indexOf(index)
            removeAt(mappedIndex)
        }

        removeMapping(index)
    }

    private fun insertMapping(index: Int) {
        mappings = mappings.flatten()
            .toMutableList()
            .apply { add(index) }
            .convertListToRanges()
    }

    private fun removeMapping(index: Int) {
        mappings = mappings.flatten()
            .toMutableList()
            .apply { remove(index) }
            .convertListToRanges()
    }

    private fun List<Int>.convertListToRanges() : List<IntRange> {
        return sorted()
            .distinct()
            .fold(listOf()) { ranges, index ->
                val last = ranges.lastOrNull()
                if (last != null) {

                    if (last.last + 1 != index) {
                        ranges.plusElement(index..index)
                    } else {
                        ranges.toMutableList()
                            .dropLast(1)
                            .plusElement(ranges.last().first..index)
                    }
                } else {
                    ranges.plusElement(index..index)
                }
            }
    }
}