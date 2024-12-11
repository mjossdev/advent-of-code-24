import kotlin.math.abs

fun main() {
    fun readLists(input: List<String>): Pair<List<Int>, List<Int>> {
        val spaces = Regex("\\s+")
        val pairs = input.map { line -> line.split(spaces).map { it.toInt() } }
        val left = pairs.map { it[0] }
        val right = pairs.map { it[1] }
        return Pair(left, right)
    }

    fun part1(input: List<String>): Int {
        val (left, right) = readLists(input)
        return left.sorted().zip(right.sorted()) { l, r -> abs(l - r) }.sum()
    }

    fun part2(input: List<String>): Int {
        val (left, right) = readLists(input)
        val counts = right.eachCount()
        return left.sumOf { it * (counts[it] ?: 0) }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
