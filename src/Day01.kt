import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.map { line -> line.split(Regex("\\s+")).map { it.toInt() } }
        val l1 = pairs.map { it[0] }.sorted()
        val l2 = pairs.map { it[1] }.sorted()
        return l1.zip(l2).sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
