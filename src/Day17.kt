fun main() {
    class Computer(
        val instructions: List<Long>,
        private var a: Long,
        private var b: Long,
        private var c: Long
    ) {
        init {
            require(instructions.all { it in 0L..7L })
        }

        private var instructionPointer = 0
        private val outputs = mutableListOf<Long>()

        fun run(): List<Long> {
            while (instructionPointer < instructions.size) {
                execute()
            }
            return outputs
        }

        private fun execute() {
            when (instructions[instructionPointer]) {
                0L -> adv()
                1L -> bxl()
                2L -> bst()
                3L -> jnz()
                4L -> bxc()
                5L -> out()
                6L -> bdv()
                7L -> cdv()
            }
        }

        private fun adv() {
            a = a shr combo().toInt()
            next()
        }

        private fun bxl() {
            b = b xor literal()
            next()
        }

        private fun bst() {
            b = combo() % 8
            next()
        }

        private fun jnz() {
            if (a == 0L) {
                next()
                return
            }
            instructionPointer = literal().toInt()
        }

        private fun bxc() {
            b = b xor c
            next()
        }

        private fun out() {
            outputs.add(combo() % 8L)
            next()
        }

        private fun bdv() {
            b = a shr combo().toInt()
            next()
        }

        private fun cdv() {
            c = a shr combo().toInt()
            next()
        }

        private fun literal() = instructions[instructionPointer + 1]

        private fun combo() = when (val operand = instructions[instructionPointer + 1]) {
            0L, 1L, 2L, 3L -> operand
            4L -> a
            5L -> b
            6L -> c
            else -> error("Invalid combo operand $operand")
        }

        private fun next() {
            instructionPointer += 2
        }
    }

    fun String.toRegister() = split(": ").last().toLong()
    fun String.toInstructions() = split(": ").last().split(',').map { it.toLong() }

    fun part1(input: List<String>): String {
        val (a, b, c) = input.take(3).map { it.toRegister() }
        val instructions = input.last().toInstructions()
        val computer = Computer(instructions, a, b, c)
        return computer.run().joinToString(",")
    }

    fun part2(input: List<String>): Long {
        val (b, c) = input.subList(1, 3).map { it.toRegister() }
        val instructions = input.last().toInstructions()
        val advIndex = instructions.indices.single{ it % 2 == 0 && instructions[it] == 0L }
        val advOperand = instructions[advIndex + 1]
        val factor = 1L shl advOperand.toInt()

        fun findA(nextA: Long, size: Int): Long? {
            val expectedResult = instructions.takeLast(size)
            for (i in 0L until factor) {
                val a = nextA * factor + i
                val result = Computer(instructions, a, b, c).run()
                if (result == expectedResult) {
                    if (size == instructions.size) {
                        return a
                    }
                    findA(a, size + 1)?.let {
                        return it
                    }
                }
            }
            return null
        }

        return findA(0L, 1)!!.also { check(Computer(instructions, it, b, c).run() == instructions) }
    }

    check(part1(readInput("Day17_test1")) == "4,6,3,5,6,3,5,2,1,0")
    check(part2(readInput("Day17_test2")) == 117440L)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
