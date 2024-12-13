fun main() {
    class Button(val cost: Int, val x: Int, val y: Int)

    class Game(val aButton: Button, val bButton: Button, val prize: Coordinate) {
        fun calculateCheapestWin(): Int? {
            val range = 1..100
            return range.asSequence().flatMap { aPresses ->
                range.mapNotNull { bPresses ->
                    val result = Coordinate(
                        aPresses * aButton.x + bPresses * bButton.x,
                        aPresses * aButton.y + bPresses * bButton.y
                    )
                    if (result == prize) {
                        aPresses * aButton.cost + bPresses * bButton.cost
                    } else {
                        null
                    }
                }
            }.minOrNull()
        }
    }

    val buttonRegex = Regex("""Button ([A|B]): X\+(\d+), Y\+(\d+)""")
    fun String.toButton(): Button {
        val matchResult = buttonRegex.matchEntire(this) ?: error("Invalid button: $this")
        val (_, name, x, y) = matchResult.groupValues
        val cost = when (name) {
            "A" -> 3
            "B" -> 1
            else -> error("Unreachable")
        }
        return Button(cost, x.toInt(), y.toInt())
    }

    val prizeRegex = Regex("""Prize: X=(\d+), Y=(\d+)""")
    fun String.toPrize(): Coordinate {
        val matchResult = prizeRegex.matchEntire(this) ?: error("Invalid prize: $this")
        val (_, x, y) = matchResult.groupValues
        return Coordinate(x.toInt(), y.toInt())
    }

    fun List<String>.toGame(): Game {
        val (aButton, bButton, prize) = this
        return Game(aButton.toButton(), bButton.toButton(), prize.toPrize())
    }

    fun part1(input: List<String>): Int = input.asSequence()
        .chunked(4)
        .sumOf { it.toGame().calculateCheapestWin() ?: 0 }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480)
//    check(part2(testInput) == 81)

    val input = readInput("Day13")
    part1(input).println()
//    part2(input).println()
}
