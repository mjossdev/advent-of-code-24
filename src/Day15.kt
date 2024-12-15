fun main() {
    fun Char.toDirection() = when (this) {
        '^' -> Direction.UP
        '>' -> Direction.RIGHT
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        else -> error("No Direction")
    }

    fun part1(input: List<String>): Int {
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

        fun Warehouse.move(directions: List<Direction>): Warehouse {
            var robot = robot
            val boxes = boxes.toMutableSet()
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
            return copy(robot = robot, boxes = boxes)
        }

        val (grid, arrows) = input.split { it.isBlank() }
        val warehouse = grid.toWarehouse()
        val directions = arrows.joinToString("").map { it.toDirection() }
        return warehouse.move(directions).boxes.sumOf { (row, col) -> row * 100 + col }
    }

    fun part2(input: List<String>): Int {
        fun Pair<Point, Point>.next(direction: Direction) = map { it.next(direction) }
        fun Iterable<Pair<Point, Point>>.toLookup() = buildMap {
            this@toLookup.forEach {
                this[it.first] = it
                this[it.second] = it
            }
        }

        data class Warehouse(val robot: Point, val boxes: Set<Pair<Point, Point>>, val walls: Set<Pair<Point, Point>>)
        fun List<String>.toWarehouse(): Warehouse {
            var robot: Point? = null
            val boxes = mutableSetOf<Pair<Point, Point>>()
            val walls = mutableSetOf<Pair<Point, Point>>()
            forEachIndexed { row, line ->
                line.forEachIndexed { col, c ->
                    val point = Point(row, col * 2)
                    val pair = point to point.next(Direction.RIGHT)
                    when (c) {
                        '@' -> robot = point
                        'O' -> boxes.add(pair)
                        '#' -> walls.add(pair)
                    }
                }
            }
            return Warehouse(robot!!, boxes, walls)
        }

        fun Warehouse.move(directions: List<Direction>): Warehouse {
            var robot = robot
            val boxes = boxes.toMutableSet()
            var boxesLookup = boxes.toLookup()
            val wallsLookup = walls.toLookup()
            for (direction in directions) {
                val nextRobot = robot.next(direction)
                if (nextRobot in wallsLookup) {
                    continue
                }
                val box = boxesLookup[nextRobot]
                if (box != null) {
                    val boxesBefore = mutableSetOf(box)
                    var nextBoxes = box.toList().mapNotNull { boxesLookup[it.next(direction)] }.filter { it != box }.toSet()
                    while (nextBoxes.isNotEmpty()) {
                        boxesBefore.addAll(nextBoxes)
                        nextBoxes = nextBoxes.flatMap { it.toList() }.mapNotNull { boxesLookup[it.next(direction)] }.filter{ it !in boxesBefore }.toSet()
                    }
                    val boxesAfter = boxesBefore.map { it.next(direction) }
                    if (boxesAfter.any { it.any { point -> point in wallsLookup } }) {
                        continue
                    }
                    boxes.removeAll(boxesBefore)
                    boxes.addAll(boxesAfter)
                    boxesLookup = boxes.toLookup()
                }
                robot = nextRobot
            }
            return copy(robot = robot, boxes = boxes)
        }

        val (grid, arrows) = input.split { it.isBlank() }
        val warehouse = grid.toWarehouse()
        val directions = arrows.joinToString("").map { it.toDirection() }
        return warehouse.move(directions).boxes.sumOf { it.first.row * 100 + it.first.col }
    }

    check(part1(readInput("Day15_test1")) == 10092)
    check(part1(readInput("Day15_test2")) == 2028)
    check(part2(readInput("Day15_test3")) == 105 + 207 + 306)
    check(part2(readInput("Day15_test1")) == 9021)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
