fun main() {
    fun List<String>.toHeights(): List<Int> {
        val char = last().first()
        val height = size
        return first().indices.map { col ->
            height - indices.first { row -> this[row][col] == char }
        }
    }

    fun part1(input: List<String>): Int {
        val schematics = input.split { it.isBlank() }
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        schematics.forEach {
            when (it.first().first()) {
                '#' -> locks
                '.' -> keys
                else -> error("invalid character")
            }.add(it.toHeights())
        }
        return locks.sumOf { lock ->
            keys.count { key ->
                key.zip(lock).all { it.first <= it.second }
            }
        }
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == 3)

    val input = readInput("Day25")
    part1(input).println()
}
