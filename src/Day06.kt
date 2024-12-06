fun main() {
    data class GuardState(val position: Point, val direction: Direction)
    data class TraversalResult(val visitedPositions: Set<Point>, val infiniteLoop: Boolean)

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

    fun GuardState.next(input: List<String>): GuardState {
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

    fun GuardState.traverse(input: List<String>): TraversalResult {
        var guardState = this
        val guardStates = mutableSetOf<GuardState>()
        while (input[guardState.position] != null && guardState !in guardStates) {
            guardStates.add(guardState)
            guardState = guardState.next(input)
        }
        return TraversalResult(
            guardStates.map { it.position }.toSet(),
            input[guardState.position] != null
        )
    }

    fun part1(input: List<String>): Int {
        val guardState = readGuardState(input)
        return guardState.traverse(input).visitedPositions.size
    }

    fun part2(input: List<String>): Int {
        val guardState = readGuardState(input)
        val traversalResult = guardState.traverse(input)
        return traversalResult.visitedPositions.count {
            if (it == guardState.position || input[it] == '#') {
                false
            } else {
                val newInput = input.toMutableList()
                newInput[it.row] = input[it.row].replaceRange(it.col..it.col, "#")
                guardState.traverse(newInput).infiniteLoop
            }
        }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
