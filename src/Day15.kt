fun main() {
    data class Warehouse(val robot: Point, val boxes: Set<Point>, val walls: Set<Point>)
    fun List<String>.toWarehouse(): Warehouse {
        var robot: Point? = null
        val boxes = mutableSetOf<Point>()
        val walls = mutableSetOf<Point>()
        forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                val point = Point(row, col)
                when (c) {
                    '@' -> robot = point
                    'O' -> boxes.add(point)
                    '#' -> walls.add(point)
                }
            }
        }
        return Warehouse(robot!!, boxes, walls)
    }

    fun Char.toDirection() = when (this) {
        '^' -> Direction.UP
        '>' -> Direction.RIGHT
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        else -> error("No Direction")
    }

    fun Warehouse.move(directions: List<Direction>): Warehouse {
        var robot = robot
        val boxes = boxes.toMutableSet()
        val walls = walls.toMutableSet()
        for (direction in directions) {
            val nextRobot = robot.next(direction)
            if (nextRobot in walls) {
                continue
            }
            if (nextRobot in boxes) {
                var nextBox = nextRobot.next(direction)
                while (nextBox in boxes) {
                    nextBox = nextBox.next(direction)
                }
                if (nextBox in walls) {
                    continue
                }
                boxes.remove(nextRobot)
                boxes.add(nextBox)
            }
            robot = nextRobot
        }
        return Warehouse(robot, boxes, walls)
    }

    fun part1(input: List<String>): Int {
        val (grid, arrows) = input.split { it.isBlank() }
        val warehouse = grid.toWarehouse()
        val directions = arrows.joinToString("").map { it.toDirection() }
        return warehouse.move(directions).boxes.sumOf { (row, col) -> row * 100 + col }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    check(part1(readInput("Day15_test1")) == 10092)
    check(part1(readInput("Day15_test2")) == 2028)
//    check(part2(testInput) == 81)

    val input = readInput("Day15")
    part1(input).println()
//    part2(input).println()
}
