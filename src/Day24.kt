private enum class Operator {
    AND, XOR, OR
}

private sealed interface Adder {
    val a: String?
    val b: String?
    val s: String?
    val cOut: String?

    data class Half(override val a: String?, override val b: String?, override val s: String?, override val cOut: String?) :
        Adder

    data class Full(val half1: Half, val half2: Half, override val cOut: String?) : Adder {
        val cIn get() = half2.a
        override val a get() = half1.a
        override val b get() = half2.b
        override val s get() = half2.s
    }
}

fun main() {
    data class LogicGate(
        val input1: String,
        val input2: String,
        val output: String,
        val operator: Operator
    ) {
        override fun toString(): String = "$input1 $operator $input2 -> $output"
    }

    class LoopException : RuntimeException()

    fun String.toLogicGate(): LogicGate {
        val (input1, operator, input2, _, output) = split(' ')
        return LogicGate(input1, input2, output, Operator.valueOf(operator))
    }

    fun List<String>.toWires() = associate {
        val (wire, value) = it.split(": ")
        wire to value.toLong()
    }

    fun Map<String, Long>.applyGates(
        gates: Collection<LogicGate>,
    ): Map<String, Long> {
        val wires = toMutableMap()
        val queue = ArrayDeque(gates)
        var inputsNotFound = 0

        while (queue.isNotEmpty()) {
            val gate = queue.removeFirst()
            if (gate.input1 !in wires || gate.input2 !in wires) {
                queue.addLast(gate)
                if (++inputsNotFound == queue.size) {
                    throw LoopException()
                }
                continue
            }

            inputsNotFound = 0
            val v1 = wires.getValue(gate.input1)
            val v2 = wires.getValue(gate.input2)
            wires[gate.output] = when (gate.operator) {
                Operator.AND -> v1 and v2
                Operator.XOR -> v1 xor v2
                Operator.OR -> v1 or v2
            }
        }
        return wires
    }

    fun Map<String, Long>.getValue(char: Char): Long {
        var result = 0L
        for (i in 0 until Long.SIZE_BITS) {
            val value = this["$char${i.toString().padStart(2, '0')}"] ?: break
            result = result or (value shl i)
        }
        return result
    }

    fun wire(char: Char, bit: Int) = "$char${bit.toString().padStart(2, '0')}"

    fun part1(input: List<String>): Long {
        val wires = input.takeWhile { it.isNotBlank() }.toWires()
        val gates = input.takeLastWhile { it.isNotBlank() }.map { it.toLogicGate() }
        return wires.applyGates(gates).getValue('z')
    }

    fun part2(input: List<String>): String {
        val wires = input.takeWhile { it.isNotBlank() }.toWires().mapValues { 0L }
        val swaps = listOf<Pair<String, String>>(
            // swaps were figured out manually, not committed to avoid sharing input
        )
        val gates = input.takeLastWhile { it.isNotBlank() }.map { it.toLogicGate() }.toMutableSet().apply {
            for (pair in swaps) {
                val g1 = first { it.output == pair.first }
                val g2 = first { it.output == pair.second }
                remove(g1)
                remove(g2)
                add(g1.copy(output = g2.output))
                add(g2.copy(output = g1.output))
            }
        }
        val gateByInputs = gates.associateBy { Pair(setOf(it.input1, it.input2), it.operator) }
        val highestInputBit = wires.keys.maxOf { it.drop(1).toInt() }

        fun getHalfAdder(a: String?, b: String?): Adder.Half {
            if (a == null || b == null) {
                return Adder.Half(a, b, null, null)
            }
            val set = setOf(a, b)
            val xor = gateByInputs[set to Operator.XOR]
            val and = gateByInputs[set to Operator.AND]
            return Adder.Half(a, b, xor?.output, and?.output)
        }

        val initial = getHalfAdder("x00", "y00")
        val adders = mutableListOf<Adder>(initial)
        for (i in 1..highestInputBit) {
            val xWire = wire('x', i)
            val yWire = wire('y', i)
            val halfAdder1 = getHalfAdder(xWire, yWire)
            val expectedCIn = adders.last().cOut
            val halfAdder2 = getHalfAdder(expectedCIn, halfAdder1.s)
            val or = if (halfAdder1.cOut != null && halfAdder2.cOut != null) {
                gateByInputs[setOf(halfAdder1.cOut, halfAdder2.cOut) to Operator.OR]
            } else {
                null
            }
            val fullAdder = Adder.Full(halfAdder1, halfAdder2, or?.output)
            adders.add(fullAdder)
        }
        adders.forEach { println(it) }
        return swaps.flatMap { it.toList() }.sorted().joinToString(",")
    }

    check(part1(readInput("Day24_test1")) == 4L)
    check(part1(readInput("Day24_test2")) == 2024L)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}
