fun main() {
    data class MulInstruction(val x: Int, val y: Int)

    fun readInstructions(input: String): List<MulInstruction> {
        val pattern = Regex("""mul\((\d+),(\d+)\)""")
        return pattern.findAll(input).map { matchResult ->
            val (x, y) = matchResult.groupValues.drop(1).map { it.toInt() }
            MulInstruction(x, y)
        }.toList()
    }

    fun part1(input: String): Int = readInstructions(input).sumOf { it.x * it.y }

    fun part2(input: String): Int {
        return 0
    }

    val testInput = readInputAsString("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 0)

    val input = readInputAsString("Day03")
    part1(input).println()
    part2(input).println()
}
