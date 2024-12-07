fun main() {
    data class Equation(val result: Long, val numbers: List<Long>)

    fun String.toEquation(): Equation {
        val (result, numbers) = split(": ")
        return Equation(
            result.toLong(),
            numbers.split(' ').map { it.toLong() }
        )
    }

    fun Equation.isValid(): Boolean {
        var possibleResults = setOf(numbers.first())
        for (n in numbers.drop(1)) {
            possibleResults = possibleResults.flatMap { listOf(it + n, it * n) }.toSet()
        }
        return result in possibleResults
    }

    fun part1(input: List<String>): Long {
        val equations = input.map { it.toEquation() }
        return equations.filter { it.isValid() }.sumOf { it.result }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
//    check(part2(testInput) == 6)

    val input = readInput("Day07")
    part1(input).println()
//    part2(input).println()
}
