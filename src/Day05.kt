fun main() {
    fun readRules(input: List<String>): Map<Int, List<Int>> = input.map { line ->
        val (page, beforePage) = line.split('|').map { it.toInt() }
        page to beforePage
    }.groupBy({ it.first }, { it.second })

    fun readUpdates(input: List<String>): List<List<Int>> = input.map { line ->
        line.split(',').map { it.toInt() }
    }

    fun part1(input: List<String>): Int {
        val (rulesInput, updatesInput) = input.split { it.isBlank() }
        val rules = readRules(rulesInput)
        val updates = readUpdates(updatesInput)
        return updates.filter { update ->
            update.allIndexed { index, page ->
                val otherPages = rules[page] ?: emptyList()
                otherPages.all {
                    val otherIndex = update.lastIndexOf(it)
                    otherIndex == -1 || otherIndex > index
                }
            }
        }.sumOf {
            it[it.size / 2]
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
//    check(part2(testInput) == 9)

    val input = readInput("Day05")
    part1(input).println()
//    part2(input).println()
}
