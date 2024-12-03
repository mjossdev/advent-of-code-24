fun main() {
    data class MulInstruction(val x: Int, val y: Int)

    fun part1(input: String): Int {
        fun readInstructions(input: String): List<MulInstruction> {
            val pattern = Regex("""mul\((\d+),(\d+)\)""")
            return pattern.findAll(input).map { matchResult ->
                val (x, y) = matchResult.groupValues.drop(1).map { it.toInt() }
                MulInstruction(x, y)
            }.toList()
        }

        return readInstructions(input).sumOf { it.x * it.y }
    }

    fun part2(input: String): Int {
        fun readInstructions(input: String): List<MulInstruction> = buildList {
            val pattern = Regex("""mul\((\d+),(\d+)\)|do(?:n't)?\(\)""")
            var enabled = true
            pattern.findAll(input).forEach { matchResult ->
                val value = matchResult.value
                when {
                    value == "do()" -> enabled = true
                    value == "don't()" -> enabled = false
                    value.startsWith("mul") -> {
                        if (enabled) {
                            val (x, y) = matchResult.groupValues.drop(1).map { it.toInt() }
                            add(MulInstruction(x, y))
                        }
                    }
                }
            }
        }

        return readInstructions(input).sumOf { it.x * it.y }
    }

    check(part1("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))") == 161)
    check(part2("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") == 48)

    val input = readInputAsString("Day03")
    part1(input).println()
    part2(input).println()
}
