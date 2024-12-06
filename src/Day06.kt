fun main() {
    data class GuardState(val position: Point, val direction: Direction)

    fun readGuardState(input: List<String>): GuardState {
        input.forEachIndexed { rowIndex, row ->
            row.indexOf('^').let {
                if (it != -1) {
                    return GuardState(Point(rowIndex, it), Direction.UP)
                }
            }
        }
        error("no guard found")
    }

    fun part1(input: List<String>): Int {
        fun GuardState.next(): GuardState {
            val nextPosition = position.let {
                when (direction) {
                    Direction.UP -> it.copy(row = it.row - 1)
                    Direction.DOWN -> it.copy(row = it.row + 1)
                    Direction.LEFT -> it.copy(col = it.col - 1)
                    Direction.RIGHT -> it.copy(col = it.col + 1)
                }
            }
            return if (input[nextPosition] == '#') {
                val nextDirection = when (direction) {
                    Direction.UP -> Direction.RIGHT
                    Direction.RIGHT -> Direction.DOWN
                    Direction.DOWN -> Direction.LEFT
                    Direction.LEFT -> Direction.UP
                }
                copy(
                    direction = nextDirection
                )
            } else {
                copy(
                    position = nextPosition
                )
            }
        }

        var guardState = readGuardState(input)
        val visitedPositions = mutableSetOf<Point>(guardState.position)
        while (input[guardState.position] != null) {
            guardState = guardState.next()
            visitedPositions.add(guardState.position)
        }
        // last position is out of bounds
        return visitedPositions.size - 1
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
//    check(part2(testInput) == 123)

    val input = readInput("Day06")
    part1(input).println()
//    part2(input).println()
}
