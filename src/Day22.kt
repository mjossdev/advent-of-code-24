fun main() {
    fun Long.prune() = this % 16777216
    fun Long.mix(transform: (Long) -> Long) = transform(this) xor this

    fun Long.evolve(): Sequence<Long> = sequence {
        var secret = this@evolve
        while (true) {
            yield(secret)
            secret = secret.mix { it shl 6 }.prune()
                .mix { it shr 5 }.prune()
                .mix { it shl 11 }.prune()
        }
    }

    fun Long.toPrice(): Int = toString().last().digitToInt()

    fun part1(input: List<String>): Long = input.sumOf {
        it.toLong().evolve().drop(2000).first()
    }

    fun part2(input: List<String>): Int {
        val priceMaps = input.map { initialSecret ->
            buildMap {
                initialSecret.toLong().evolve()
                    .take(2001)
                    .map { it.toPrice() }
                    .zipWithNext()
                    .windowed(4)
                    .forEach {
                        val changes = it.map { (prev, next) -> next - prev }
                        if (changes !in this) {
                            this[changes] = it.last().second
                        }
                    }
            }
        }
        val allSequences = priceMaps.flatMap { it.keys }.toSet()
        return allSequences.parallelStream().mapToInt {sequence ->
            priceMaps.sumOf { it[sequence] ?: 0 }
        }.max().asInt
    }

    check(part1(readInput("Day22_test1")) == 37327623L)
    check(part2(readInput("Day22_test2")) == 23)

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
