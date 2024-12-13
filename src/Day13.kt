fun main() {
    data class Button(val cost: Int, val x: Int, val y: Int)

    data class Game(val aButton: Button, val bButton: Button, val prize: Coordinate)

    fun Game.calculateCheapestWin(maxPresses: Int? = null, offset: Long = 0L): Long? {
        val px = prize.x + offset
        val py = prize.y + offset
        // equation was solved on paper
        val numerator = px * aButton.y - py * aButton.x
        val denominator = bButton.x * aButton.y - bButton.y * aButton.x
        val bPresses = numerator / denominator
        val aPresses = (px - bPresses * bButton.x) / aButton.x
        if (maxPresses != null && (bPresses > maxPresses || aPresses > maxPresses)
            || aPresses * aButton.x + bPresses * bButton.x != px
            || aPresses * aButton.y + bPresses * bButton.y != py
            ) {
            return null
        }
        return aPresses * aButton.cost + bPresses * bButton.cost
    }

    val buttonRegex = Regex("""Button ([AB]): X\+(\d+), Y\+(\d+)""")
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

    fun part1(input: List<String>): Long = input.chunked(4)
        .sumOf { it.toGame().calculateCheapestWin(maxPresses = 100) ?: 0 }

    fun part2(input: List<String>): Long = input.chunked(4)
        .sumOf { it.toGame().calculateCheapestWin(offset = 10000000000000) ?: 0 }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
