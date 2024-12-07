fun main() {
    data class Equation(val result: Long, val numbers: List<Long>)

    fun String.toEquation(): Equation {
        val (result, numbers) = split(": ")
        return Equation(
            result.toLong(),
            numbers.split(' ').map { it.toLong() }
        )
    }

    fun solve(input: List<String>, vararg operators: (Long, Long) -> Long): Long {
        fun Equation.isValid(): Boolean {
            var possibleResults = setOf(numbers.first())
            for (n in numbers.drop(1)) {
                possibleResults = possibleResults.flatMap { operators.map { op -> op(it, n) } }.toSet()
            }
            return result in possibleResults
        }

        val equations = input.map { it.toEquation() }
        return equations.filter { it.isValid() }.sumOf { it.result }
    }

    fun part1(input: List<String>): Long = solve(input, Long::plus, Long::times)

    fun part2(input: List<String>): Long = solve(input, Long::plus, Long::times, { a, b -> "$a$b".toLong() })

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
