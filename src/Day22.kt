fun main() {
    fun Long.prune() = this % 16777216
    fun Long.mix(transform: (Long) -> Long) = transform(this) xor this

    fun Long.evolve(steps: Int): Long {
        var secret = this
        repeat(steps) {
            secret = secret.mix { it shl 6 }.prune()
                .mix { it shr 5 }.prune()
                .mix { it shl 11 }.prune()
        }
        return secret
    }

    fun part1(input: List<String>): Long = input.sumOf {
        it.toLong().evolve(2000)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 37327623L)
//    check(part2(testInput) == 16)

    val input = readInput("Day22")
    part1(input).println()
//    part2(input).println()
}
