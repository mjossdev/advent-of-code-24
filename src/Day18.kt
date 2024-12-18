fun main() {
    fun findPath(blocked: Set<Coordinate>, range: IntRange): Sequence<Coordinate>? {
        val root = Coordinate(0, 0)
        val end = Coordinate(range.last, range.last)
        val parents = mutableMapOf<Coordinate, Coordinate>()
        val explored = mutableSetOf(root)
        val queue = ArrayDeque(listOf(root))
        while (queue.isNotEmpty()) {
            val v = queue.removeFirst()
            if (v == end) {
                return generateSequence(end) { parents[it] }
            }
            Direction.entries.map { v.next(it) }
                .filter { it.x in range && it.y in range && it !in blocked && it !in explored }
                .forEach {
                    explored.add(it)
                    parents[it] = v
                    queue.addLast(it)
                }
        }
        return null
    }

    fun String.toCoordinate(): Coordinate {
        val (x, y) = split(',').map { it.toInt() }
        return Coordinate(x, y)
    }

    fun part1(input: List<String>, range: IntRange): Int {
        val blocked = input.map { it.toCoordinate() }.toSet()
        return findPath(blocked, range)!!.count() - 1
    }

    fun part2(input: List<String>, range: IntRange): String {
        val coordinates = input.map { it.toCoordinate() }
        val blocked = coordinates.toMutableSet()
        for (toRemove in coordinates.asReversed()) {
            blocked.remove(toRemove)
            if (findPath(blocked, range) != null) {
                return "${toRemove.x},${toRemove.y}"
            }
        }
        error("no")
    }

    val testInput = readInput("Day18_test")
    val testRange = 0..6
    check(part1(testInput.take(12), testRange) == 22)
    check(part2(testInput, testRange) == "6,1")

    val input = readInput("Day18")
    val range = 0..70
    part1(input.take(1024), range).println()
    part2(input, range).println()
}
