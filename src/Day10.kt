fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { l -> l.map { it.digitToInt() } }
        fun Point.traverse(expectedElevation: Int = 0, reachedPoints: Set<Point> = emptySet()): Set<Point> {
            return if (grid[this] == expectedElevation) {
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
        }
        return grid.points().sumOf { points -> points.traverse().count { grid[it] == 9 } }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
//    check(part2(testInput) == 34)

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}
