import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun readReports(input: List<String>): List<List<Int>> = input.map { line ->
        line.split(' ').map { it.toInt() }
    }

    fun List<Int>.isSafe(): Boolean {
        val sign = (this[1] - this[0]).sign
        return zipWithNext { a, b -> b - a }.all { it.sign == sign && abs(it) in 1..3 }
    }

    fun List<Int>.isSafeWithDampener(): Boolean = isSafe() || indices.any { i ->
        toMutableList().let {
            it.removeAt(i)
            it.isSafe()
        }
    }

    fun part1(input: List<String>): Int {
        val reports = readReports(input)
        return reports.count { it.isSafe() }
    }

    fun part2(input: List<String>): Int {
        val reports = readReports(input)
        return reports.count { it.isSafeWithDampener() }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
