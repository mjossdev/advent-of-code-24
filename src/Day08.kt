fun main() {
    data class Antenna(val frequency: Char, val position: Point)

    fun readAntennas(input: List<String>) = buildList {
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, c ->
                if (c.isLetterOrDigit()) {
                    add(Antenna(c, Point(rowIndex, colIndex)))
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val rowRange = input.indices
        val colRange = input.first().indices
        val antennas = readAntennas(input)
        val positionsByFrequency = antennas.groupBy({ it.frequency }, { it.position })
        return positionsByFrequency.values.flatMap { positions ->
            positions.flatMap { start ->
                positions.asSequence().filter { it != start }.map {
                    val rowDelta = it.row - start.row
                    val colDelta = it.col - start.col
                    Point(it.row + rowDelta, it.col + colDelta)
                }.filter {
                    it.row in rowRange && it.col in colRange
                }
            }
        }.toSet().size
    }

    fun Point.next(rowDelta: Int, colDelta: Int) = Point(row + rowDelta, col + colDelta)

    fun part2(input: List<String>): Int {
        val rowRange = input.indices
        val colRange = input.first().indices
        val antennas = readAntennas(input)
        val positionsByFrequency = antennas.groupBy({ it.frequency }, { it.position })
        return positionsByFrequency.values.flatMap { positions ->
            positions.flatMap { start ->
                positions.asSequence().filter { it != start }.flatMap {
                    val rowDelta = it.row - start.row
                    val colDelta = it.col - start.col
                    sequence {
                        var current = it
                        while (current.row in rowRange && current.col in colRange) {
                            yield(current)
                            current = current.next(rowDelta, colDelta)
                        }
                    }
                }
            }
        }.toSet().size
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
