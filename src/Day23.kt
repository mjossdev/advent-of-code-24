fun main() {
    fun List<String>.toNetworkMap(): Map<String, Set<String>> = buildMap<String, MutableSet<String>> {
        this@toNetworkMap.forEach {
            val (l, r) = it.split('-')
            getOrPut(l) { mutableSetOf() }.add(r)
            getOrPut(r) { mutableSetOf() }.add(l)
        }
    }

    fun part1(input: List<String>): Int {
        val networkMap = input.toNetworkMap()
        val triples = buildSet {
            networkMap.asSequence()
                .filter { it.key.startsWith('t') }
                .forEach { (first, others) ->
                    others.forEach { second ->
                        others.asSequence()
                            .filter { it != second }
                            .forEach { third ->
                                if (third in networkMap.getValue(second)) {
                                    add(setOf(first, second, third))
                                }
                            }
                    }
                }
        }
        return triples.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
//    check(part2(testInput) == 16L)

    val input = readInput("Day23")
    part1(input).println()
//    part2(input).println()
}
