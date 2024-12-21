private sealed interface Button {
    data object A : Button
    data object Up : Button {
        override fun toString(): String = "^"
    }

    data object Down : Button {
        override fun toString(): String = "v"
    }

    data object Left : Button {
        override fun toString(): String = "<"
    }

    data object Right : Button {
        override fun toString(): String = ">"
    }

    data class Number(val value: Int) : Button {
        override fun toString(): String = value.toString()
    }
}


fun main() {
    data class Result(val transitionCounts: Map<Pair<Button, Button>, Long>) {
        val totalPresses = transitionCounts.values.sum()
    }
    fun Result.merge(other: Result) = Result(
        transitionCounts.toMutableMap().apply {
            other.transitionCounts.forEach {
                merge(it.key, it.value, Long::plus)
            }
        },
    )
    fun List<Button>.toResult(times: Long = 1L): Result = Result(
        (listOf(Button.A) + this).zipWithNext().eachCount().mapValues { it.value.toLong() * times },
    )
    fun Iterable<Result>.prune(): List<Result> {
        val min = minOf { it.totalPresses }
        return filter { it.totalPresses == min }
    }


    // @formatter:off
    val numberPad = mapOf(
        Button.Number(7) to Point(0, 0), Button.Number(8) to Point(0, 1), Button.Number(9) to Point(0, 2),
        Button.Number(4) to Point(1, 0), Button.Number(5) to Point(1, 1), Button.Number(6) to Point(1, 2),
        Button.Number(1) to Point(2, 0), Button.Number(2) to Point(2, 1), Button.Number(3) to Point(2, 2),
                                                        Button.Number(0) to Point(3, 1), Button.A to Point(3, 2),
    )
    val numberGap = Point(3, 0)

    val directionPad = mapOf(
                                             Button.Up to Point(0, 1),   Button.A to Point(0, 2),
        Button.Left to Point(1, 0), Button.Down to Point(1, 1), Button.Right to Point(1, 2)
    )
    val directionGap = Point(0, 0)
    // @formatter:on

    fun Result.remoteResults(targetPad: Map<Button, Point>, gap: Point): List<Result> {
        var possibilities = listOf(Result(emptyMap()))
        for ((transition, count) in transitionCounts) {
            val (current, next) = transition
            if (current == next) {
                possibilities = possibilities.map { it.merge(listOf(Button.A).toResult(count)) }
                continue
            }
            val currentPos = targetPad.getValue(current)
            val nextPos = targetPad.getValue(next)
            val verticalPresses = when {
                nextPos.row > currentPos.row -> List(nextPos.row - currentPos.row) { Button.Down }
                nextPos.row < currentPos.row -> List(currentPos.row - nextPos.row) { Button.Up }
                else -> emptyList()
            }
            val horizontalPresses = when {
                nextPos.col > currentPos.col -> List(nextPos.col - currentPos.col) { Button.Right }
                nextPos.col < currentPos.col -> List(currentPos.col - nextPos.col) { Button.Left }
                else -> emptyList()
            }
            val options = sequence {
                if (nextPos.row != gap.row || currentPos.col != gap.col) {
                    yield((verticalPresses + horizontalPresses + Button.A).toResult(count))
                }
                if (nextPos.col != gap.col || currentPos.row != gap.row) {
                    yield((horizontalPresses + verticalPresses + Button.A).toResult(count))
                }
            }.toSet()
            possibilities = possibilities.flatMap { result ->
                options.map { result.merge(it) }
            }
        }
        return possibilities
    }

    fun String.getNumberRobotResults(): List<Result> {
        val buttons = map {
            if (it.isDigit()) {
                Button.Number(it.digitToInt())
            } else {
                check(it == 'A')
                Button.A
            }
        }
        return buttons.toResult().remoteResults(numberPad, numberGap)
    }

    fun solve(input: List<String>, robots: Int): Long = input.sumOf { code ->
        var results = code.getNumberRobotResults()
        repeat(robots) {
            results = results.flatMap {
                it.remoteResults(directionPad, directionGap)
            }.prune()
        }
        results.first().totalPresses * code.filter { it.isDigit() }.toLong()
    }

    fun part1(input: List<String>): Long = solve(input, 2)
    fun part2(input: List<String>): Long = solve(input, 25)
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 126384L)

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}
