private enum class Operator {
    AND, XOR, OR
}

fun main() {
    data class LogicGate(
        val input1: String,
        val input2: String,
        val output: String,
        val operator: Operator
    )

    fun String.toLogicGate(): LogicGate {
        val (input1, operator, input2, _, output) = split(' ');
        return LogicGate(input1, input2, output, Operator.valueOf(operator))
    }

    fun Map<String, Long>.applyGates(gates: List<LogicGate>): Map<String, Long> {
        val wires = toMutableMap()
        val queue = ArrayDeque(gates)

        while (queue.isNotEmpty()) {
            val gate = queue.removeFirst()
            if (gate.input1 !in wires || gate.input2 !in wires) {
                queue.addLast(gate)
                continue
            }
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

    fun part1(input: List<String>): Long {
        val wires = input.takeWhile { it.isNotBlank() }.associate {
            val (wire, value) = it.split(": ")
            wire to value.toLong()
        }
        val gates = input.takeLastWhile { it.isNotBlank() }.map { it.toLogicGate() }

        return wires.applyGates(gates).filter { it.key.startsWith('z') }
            .map { (wire, value) ->
                val bit = wire.drop(1).toInt()
                (value shl bit)
            }.fold(0L, Long::or)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    check(part1(readInput("Day24_test1")) == 4L)
    check(part1(readInput("Day24_test2")) == 2024L)

    val input = readInput("Day24")
    part1(input).println()
//    part2(input).println()
}
