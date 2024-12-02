import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun readReports(input: List<String>): List<List<Int>> = input.map { line ->
        line.split(' ').map { it.toInt() }
    }

    fun part1(input: List<String>): Int {
        val reports = readReports(input)
        return reports.count { report ->
            val sign = (report[1] - report[0]).sign
            report.zipWithNext { a, b -> b - a }.all { it.sign == sign && abs(it) in 1..3 }
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
//    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
