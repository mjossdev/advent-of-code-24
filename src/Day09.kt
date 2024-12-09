import kotlin.math.min

fun main() {
    data class Block(val size: Int, val fileId: Int? = null) {
        override fun toString(): String = (fileId?.toString() ?: ".").repeat(size)
    }

    fun String.toBlocks(): List<Block> = mapIndexed { index, c ->
        val size = c.digitToInt()
        val fileId = if (index % 2 == 0) {
            index / 2
        } else {
            null
        }
        Block(size, fileId)
    }

    fun List<Block>.freeIndex() = indexOfFirst { it.fileId == null }
    fun MutableList<Block>.removeTrailingFree() {
        while (last().fileId == null) {
            removeLast()
        }
    }
    fun List<Block>.checksum() = flatMap { block ->
        List(block.size) { block.fileId?.toLong() ?: 0L }
    }.sumOfIndexed { index, fileId -> index * fileId }

    fun part1(input: String): Long {
        val fileSystem = input.toBlocks().toMutableList()
        fileSystem.removeTrailingFree()
        var freeIndex = fileSystem.freeIndex()
        while (freeIndex != -1) {
            var targetIndex = freeIndex
            var remainingFree = fileSystem.removeAt(freeIndex).size
            while (remainingFree > 0) {
                fileSystem.removeTrailingFree()
                val file: Block = fileSystem.removeLast()
                val sizeToMove = min(remainingFree, file.size)
                fileSystem.add(targetIndex, file.copy(size = sizeToMove))
                if (file.size > sizeToMove) {
                    fileSystem.add(file.copy(size = file.size - sizeToMove))
                }
                remainingFree -= sizeToMove
                ++targetIndex
            }
            freeIndex = fileSystem.freeIndex()
            fileSystem.removeTrailingFree()
        }
        return fileSystem.checksum()
    }

    fun part2(input: String): Long {
        val fileSystem = input.toBlocks().toMutableList()
        val placeholder = Block(0)
        for (fileId in fileSystem.last { it.fileId != null }.fileId!! downTo 0) {
            val fileIndex = fileSystem.indexOfLast { it.fileId == fileId }
            val file = fileSystem[fileIndex]
            val freeIndex = fileSystem.indexOfFirst { it.fileId == null && it.size >= file.size }
            if (freeIndex == -1 || freeIndex > fileIndex) {
                continue
            }
            val freeSpace = fileSystem[freeIndex]
            fileSystem[freeIndex] = file
            val freeSpaceBefore = fileSystem.getOrNull(fileIndex - 1)?.takeIf { it.fileId == null }?.size ?: 0
            if (freeSpaceBefore > 0) {
                fileSystem[fileIndex - 1] = placeholder
            }
            val freeSpaceAfter = fileSystem.getOrNull(fileIndex + 1)?.takeIf { it.fileId == null }?.size ?: 0
            if (freeSpaceAfter > 0) {
                fileSystem[fileIndex + 1] = placeholder
            }
            fileSystem[fileIndex] = Block(file.size + freeSpaceBefore + freeSpaceAfter)
            if (freeSpace.size > file.size) {
                fileSystem.add(freeIndex + 1, Block(freeSpace.size - file.size))
            }
        }
        fileSystem.removeAll { it.size == 0 }
        return fileSystem.checksum()
    }

    val testInput = "2333133121414131402"
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInputAsString("Day09")
    part1(input).println()
    part2(input).println()
}
