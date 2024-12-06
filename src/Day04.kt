fun main() {
    data class Vector(val rowDelta: Int, val colDelta: Int)

    operator fun Vector.times(factor: Int) = Vector(rowDelta * factor, colDelta * factor)
    operator fun Point.plus(vector: Vector) = Point(row + vector.rowDelta, col + vector.colDelta)

    fun part1(input: List<String>): Int {
        fun List<String>.hasWordAt(word: String, start: Point, direction: Vector): Boolean {
            word.forEachIndexed { index, c ->
                if (this[start + direction * index] != c) {
                    return false
                }
            }
            return true
        }

        val range = -1..1
        return input.indices.sumOf { row ->
            input[row].indices.sumOf { col ->
                range.sumOf { rowDelta ->
                    range.count { colDelta ->
                        input.hasWordAt("XMAS", Point(row, col), Vector(rowDelta, colDelta))
                    }
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val neighbors = setOf('M', 'S')
        val topLeft = Vector(-1, -1)
        val topRight = Vector(-1, 1)
        val bottomLeft = Vector(1, -1)
        val bottomRight = Vector(1, 1)
        return input.indices.sumOf { row ->
            input[row].indices.count { col ->
                val middle = Point(row, col)
                input[middle] == 'A'
                        && setOf(input[middle + topLeft], input[middle + bottomRight]) == neighbors
                        && setOf(input[middle + topRight], input[middle + bottomLeft]) == neighbors
            }
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
