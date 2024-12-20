fun main() {
    fun List<String>.cheats(maxLength: Int, minSave: Int = 1): Map<Int, Int> {
        val (start, end) = findStartAndEnd(this)
        val dist = mutableMapOf(start to 0)
        val path = mutableListOf(start)
        var current = start
        while (current != end) {
            val next = current.neighbors().single { this[it] != '#' && it !in dist }
            dist[next] = dist.getValue(current) + 1
            path.add(next)
            current = next
        }
        return path.dropLast(minSave).asSequence()
            .flatMapIndexed { index, cheatStart ->
                path.subList(index + minSave, path.size)
                    .mapNotNull { cheatEnd ->
                        if (cheatStart.distance(cheatEnd) > maxLength) {
                            return@mapNotNull null
                        }
                        val cheatLength = cheatStart.distance(cheatEnd)
                        val posWithoutCheat = path[index + cheatLength]
                        (dist.getValue(cheatEnd) - dist.getValue(posWithoutCheat)).takeIf { it >= minSave }
                    }
            }.eachCount()
    }

    fun part1(input: List<String>): Int = input.cheats(2, 100).values.sum()

    fun part2(input: List<String>): Int = input.cheats(20, 100).values.sum()

    val testInput = readInput("Day20_test")
    check(
        testInput.cheats(2) == mapOf(
            2 to 14,
            4 to 14,
            6 to 2,
            8 to 4,
            10 to 2,
            12 to 3,
            20 to 1,
            36 to 1,
            38 to 1,
            40 to 1,
            64 to 1
        )
    )
    check(
        testInput.cheats(20, 50) == mapOf(
            50 to 32,
            52 to 31,
            54 to 29,
            56 to 39,
            58 to 25,
            60 to 23,
            62 to 20,
            64 to 19,
            66 to 12,
            68 to 14,
            70 to 12,
            72 to 22,
            74 to 4,
            76 to 3
        )
    )

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
