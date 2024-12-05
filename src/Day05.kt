fun main() {
    fun readRules(input: List<String>): Map<Int, List<Int>> = input.map { line ->
        val (page, beforePage) = line.split('|').map { it.toInt() }
        page to beforePage
    }.groupBy({ it.first }, { it.second })

    fun readUpdates(input: List<String>): List<List<Int>> = input.map { line ->
        line.split(',').map { it.toInt() }
    }

    fun List<Int>.isCorrectlyOrdered(rules: Map<Int, List<Int>>) = allIndexed { index, page ->
        val otherPages = rules[page] ?: emptyList()
        otherPages.all {
            val otherIndex = lastIndexOf(it)
            otherIndex == -1 || otherIndex > index
        }
    }

    fun part1(input: List<String>): Int {
        val (rulesInput, updatesInput) = input.split { it.isBlank() }
        val rules = readRules(rulesInput)
        val updates = readUpdates(updatesInput)
        return updates.filter { it.isCorrectlyOrdered(rules) }.sumOf {
            it[it.size / 2]
        }
    }

    fun part2(input: List<String>): Int {
        val (rulesInput, updatesInput) = input.split { it.isBlank() }
        val rules = readRules(rulesInput)
        val updates = readUpdates(updatesInput)
        return updates.filter {
            !it.isCorrectlyOrdered(rules)
        }.map { update ->
            val newUpdate = update.toMutableList()
            update.forEach { page ->
                val otherPages = rules[page] ?: emptyList()
                val currentIndex = newUpdate.indexOf(page)
                otherPages.asSequence().map { newUpdate.indexOf(it) }.filter { it != -1 }.minOrNull()?.let {
                    if (it < currentIndex) {
                        newUpdate.removeAt(currentIndex)
                        newUpdate.add(it, page)
                    }
                }
            }
            newUpdate
        }.sumOf {
            it[it.size / 2]
        }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
