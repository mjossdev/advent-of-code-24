fun main() {
    fun String.toStones() = split(' ').map { it.toLong() }
    fun Long.next(): List<Long> {
        if (this == 0L) {
            return listOf(1L)
        }
        val str = toString()
        return when (str.length % 2) {
            0 -> {
                val mid = str.length / 2
                listOf(
                    str.substring(0, mid).toLong(),
                    str.substring(mid).toLong()
                )
            }
            1 -> listOf(this * 2024L)
            else -> error("😡😡😡 y no exhaustive Kotlin 😡😡😡")
        }
    }
    fun List<Long>.blink(times: Int): Long {
        var current = eachCount().mapValues { it.value.toLong() }
        repeat(times) {
            current = buildMap {
                current.forEach { (n, count) ->
                    n.next().forEach {
                        merge(it, count, Long::plus)
                    }
                }
            }
        }
        return current.values.sum()
    }

    fun part1(input: String): Long = input.toStones().blink(25)

    fun part2(input: String): Long = input.toStones().blink(75)

    val testInput = "125 17"
    check(part1(testInput) == 55312L)

    val input = readInputAsString("Day11")
    part1(input).println()
    part2(input).println()
}
