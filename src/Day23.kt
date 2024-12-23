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

    fun part2(input: List<String>): String {
        val networkMap = input.toNetworkMap()
        val bigOnesFirst = networkMap.toList().sortedBy { it.second.size }

        val cache = mutableMapOf<Pair<Set<String>, Set<String>>, Set<String>>()
        fun findBiggestLan(current: Set<String>, candidates: Set<String>): Set<String> =
            cache.getOrPut(current to candidates) {
                if (candidates.isEmpty()) {
                    current
                } else {
                    candidates.asSequence()
                        .filter { candidate -> current.all { candidate in networkMap.getValue(it) } }
                        .map { findBiggestLan(current + it, candidates - it) }
                        .maxByOrNull { it.size } ?: current
                }
            }

        var biggestLan = emptySet<String>()
        for ((first, others) in bigOnesFirst) {
            if (others.size + 1 < biggestLan.size) {
                break
            }
            val lan = findBiggestLan(setOf(first), others)
            if (lan.size > biggestLan.size) {
                biggestLan = lan
            }
        }
        return biggestLan.sorted().joinToString(",")
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
