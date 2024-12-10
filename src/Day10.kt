fun main() {
    fun readMap(input: List<String>) = input.map { l -> l.map { it.digitToInt() } }

    fun part1(input: List<String>): Int {
        val map = readMap(input)
        fun Point.traverse(expectedElevation: Int = 0, reachedPoints: Set<Point> = emptySet()): Set<Point> =
            if (map[this] == expectedElevation) {
                val points = reachedPoints + this
                if (expectedElevation == 9) {
                    points
                } else {
                    neighbors()
                        .filter { it !in reachedPoints }
                        .flatMap { it.traverse(expectedElevation + 1, points) }
                        .toSet()
                }
            } else {
                reachedPoints
            }
        return map.points().sumOf { point -> point.traverse().count { map[it] == 9 } }
    }

    fun part2(input: List<String>): Int {
        val map = readMap(input)
        fun Point.rating(expectedElevation: Int = 0, reachedPoints: Set<Point> = emptySet()): Int =
            if (map[this] == expectedElevation) {
                if (expectedElevation == 9) {
                    1
                } else {
                    neighbors()
                        .filter { it !in reachedPoints }
                        .sumOf { it.rating(expectedElevation + 1, reachedPoints + this) }
                }
            } else {
                0
            }
        return map.points().sumOf { it.rating() }
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
