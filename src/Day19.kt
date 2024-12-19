fun main() {
    class ArrangementCounter(val towels: List<String>, val pattern: String) {
        private val cache = LongArray(pattern.length) { -1L }

        fun count(startIndex: Int = 0): Long {
            if (startIndex == pattern.length) {
                return 1L
            }
            if (cache[startIndex] == -1L) {
                cache[startIndex] = towels.sumOf {
                    if (pattern.startsWith(it, startIndex)) {
                        count(startIndex + it.length)
                    } else {
                        0L
                    }
                }
            }
            return cache[startIndex]
        }
    }

    fun part1(input: List<String>): Int {
        val regex = Regex("^(${input.first().replace(", ", "|")})*$")
        val patterns = input.drop(2)
        return patterns.count { regex matches it }
    }

    fun part2(input: List<String>): Long {
        val towels = input.first().split(", ")
        val patterns = input.drop(2)
        return patterns.sumOf { ArrangementCounter(towels, it).count() }
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
