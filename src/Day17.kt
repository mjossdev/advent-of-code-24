fun main() {
    class Computer(
        private val instructions: List<Int>,
        private var a: Int,
        private var b: Int,
        private var c: Int
    ) {
        init {
            check(instructions.all { it in 0..7 })
        }

        private var instructionPointer = 0
        private val outputs = mutableListOf<Int>()

        fun run(): String {
            while (instructionPointer < instructions.size) {
                val ipOld = instructionPointer
                execute()
                if (instructionPointer == ipOld) {
                    instructionPointer += 2
                }
            }
            return outputs.joinToString(",")
        }

        private fun execute() {
            when (instructions[instructionPointer]) {
                0 -> adv()
                1 -> bxl()
                2 -> bst()
                3 -> jnz()
                4 -> bxc()
                5 -> out()
                6 -> bdv()
                7 -> cdv()
            }
        }

        private fun adv() {
            val denominator = 1 shl combo()
            a /= denominator
        }

        private fun bxl() {
            b = b xor literal()
        }

        private fun bst() {
            b = combo() % 8
        }

        private fun jnz() {
            if (a == 0) return
            instructionPointer = literal()
        }

        private fun bxc() {
            b = b xor c
        }

        private fun out() {
            outputs.add(combo() % 8)
        }

        private fun bdv() {
            val denominator = 1 shl combo()
            b = a / denominator
        }

        private fun cdv() {
            val denominator = 1 shl combo()
            c = a / denominator
        }

        private fun literal() = instructions[instructionPointer + 1]

        private fun combo() = when (val operand = instructions[instructionPointer + 1]) {
            0, 1, 2, 3 -> operand
            4 -> a
            5 -> b
            6 -> c
            else -> error("Invalid combo operand $operand")
        }
    }

    fun part1(input: List<String>): String {
        val (a, b, c) = input.take(3).map { line -> line.split(": ")[1].toInt() }
        val instructions = input.last().split(": ")[1].split(',').map { it.toInt() }
        val computer = Computer(instructions, a, b, c)
        return computer.run()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
//    check(part2(testInput) == 81)

    val input = readInput("Day17")
    part1(input).println()
//    part2(input).println()
}
