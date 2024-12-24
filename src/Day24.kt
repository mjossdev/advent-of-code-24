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

    fun Collection<LogicGate>.swapOutputs(a: String, b: String): Set<LogicGate> = toMutableSet().apply {
        val g1 = first { it.output == a }
        val g2 = first { it.output == b }
        remove(g1)
        remove(g2)
        add(g1.copy(output = b))
        add(g2.copy(output = a))
    }

    fun part2(input: List<String>): String {
        val wires = input.takeWhile { it.isNotBlank() }.toWires().mapValues { 0L }
        val gates = input.takeLastWhile { it.isNotBlank() }.map { it.toLogicGate() }
        val highestInputBit = wires.keys.maxOf { it.drop(1).toInt() }

        fun nextSwap(gates: Collection<LogicGate>): Pair<String, String>?
        {
            val gateByInputs = gates.associateBy { Pair(setOf(it.input1, it.input2), it.operator) }

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
            if (initial.s != "z00") {
                // only possible error here
                return initial.s!! to initial.cOut!!
            }
            val adders = mutableListOf<Adder>(initial)
            for (i in 1..highestInputBit) {
                val halfAdder1 = getHalfAdder(wire('x', i), wire('y', i))
                val cIn = adders.last().cOut
                val halfAdder2 = getHalfAdder(cIn, halfAdder1.s)
                if (halfAdder2.s == null || halfAdder2.cOut == null) {
                    val gateForCIn = gates.first { it.input1 == cIn || it.input2 == cIn }
                    val toSwap = if (gateForCIn.input1 == cIn) {
                        gateForCIn.input2
                    } else {
                        gateForCIn.input1
                    }
                    return halfAdder1.s!! to toSwap
                }
                val expectedS = wire('z', i)
                if (halfAdder2.s != expectedS) {
                    return halfAdder2.s to expectedS
                }
                val or = gateByInputs[setOf(halfAdder1.cOut, halfAdder2.cOut) to Operator.OR]
                    ?: return halfAdder2.s to halfAdder2.cOut
                val fullAdder = Adder.Full(halfAdder1, halfAdder2, or.output)
                if (fullAdder.s != wire('z', i)) {
                    return fullAdder.s!! to fullAdder.cOut!!
                }
                adders.add(fullAdder)
            }
            adders.forEach(::println)
            return null
        }

        val swappedOutputs = mutableListOf<String>()
        var currentGates: Collection<LogicGate> = gates
        repeat(4) {
            val swap = nextSwap(currentGates)!!
            swappedOutputs.add(swap.first)
            swappedOutputs.add(swap.second)
            currentGates = currentGates.swapOutputs(swap.first, swap.second)
        }
        nextSwap(currentGates)
        return swappedOutputs.sorted().joinToString(",")
    }

    check(part1(readInput("Day24_test1")) == 4L)
    check(part1(readInput("Day24_test2")) == 2024L)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}
