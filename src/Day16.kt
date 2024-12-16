import java.util.PriorityQueue

fun main() {
    fun Direction.next(): List<Direction> = when(this) {
        Direction.UP, Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
        Direction.LEFT, Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
    } + this

    fun Direction.turnCost(other: Direction) = when (setOf(this, other)) {
        setOf(Direction.UP, Direction.DOWN), setOf(Direction.LEFT, Direction.RIGHT) -> 2000
        setOf(Direction.UP, Direction.LEFT), setOf(Direction.UP, Direction.RIGHT), setOf(Direction.DOWN, Direction.LEFT), setOf(Direction.DOWN, Direction.RIGHT) -> 1000
        setOf(Direction.UP), setOf(Direction.DOWN), setOf(Direction.LEFT), setOf(Direction.RIGHT) -> 0
        else -> error("unreachable")
    }

    data class State(val position: Point, val direction: Direction)
    fun findStartAndEnd(input: List<String>): Pair<Point, Point> {
        var start: Point? = null
        var end: Point? = null
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                val point = Point(row, col)
                when (input[point]) {
                    'S' -> start = point
                    'E' -> end = point
                }
            }
        }
        return start!! to end!!
    }

    fun part1(input: List<String>): Int {
        val (start, end) = findStartAndEnd(input)
        val startState = State(start, Direction.RIGHT)
        val queue = PriorityQueue<Pair<State, Int>>(Comparator.comparingInt { it.second })
        val validPoints = input.points().filter { input[it] != '#' }
        val dist = mutableMapOf(startState to 0).withDefault { Int.MAX_VALUE }
        queue.addAll(Direction.entries.map {
            startState.copy(direction = it) to startState.direction.turnCost(it)
        })
        while (queue.isNotEmpty()) {
            val u = queue.remove().first
            val nextStates = (u.direction.next()
                .map { u.copy(direction = it) to u.direction.turnCost(it) } + (u.copy(position = u.position.next(u.direction)) to 1))
                .filter { it.first.position in validPoints }
            nextStates.forEach { (v, d) ->
                val alt = dist.getValue(u) + d
                if (v.position == end) {
                    return alt
                }
                if (alt < dist.getValue(v)) {
                    dist[v] = alt
                    queue.add(v to alt)
                }
            }
        }
        error("end not found")
    }

    fun part2(input: List<String>): Int {
        val (start, end) = findStartAndEnd(input)
        val startState = State(start, Direction.RIGHT)
        val queue = PriorityQueue<Pair<State, Int>>(Comparator.comparingInt { it.second })
        val validPoints = input.points().filter { input[it] != '#' }
        val dist = mutableMapOf(startState to 0).withDefault { Int.MAX_VALUE }
        val prev = mutableMapOf<State, List<State>>()
        queue.addAll(Direction.entries.map {
            startState.copy(direction = it) to startState.direction.turnCost(it)
        })
        var done = false
        while (queue.isNotEmpty()) {
            val u = queue.remove().first
            val nextStates = listOf(u.copy(position = u.position.next(u.direction)) to 1) + u.direction.next()
                .map { u.copy(direction = it) to u.direction.turnCost(it) }
            nextStates.filter { it.first.position in validPoints }.forEach { (v, d) ->
                val alt = dist.getValue(u) + d
                val vDist = dist.getValue(v)
                if (alt > vDist && v.position == end) {
                    done = true
                }
                if (alt < vDist) {
                    prev[v] = listOf(u)
                    dist[v] = alt
                    queue.add(v to alt)
                } else if (alt == vDist) {
                    prev.merge(v, listOf(u), List<State>::plus)
                }
            }
            if (done) {
                break
            }
        }
        check(done)
        val states = mutableSetOf<State>()
        val endStates = Direction.entries.map { State(end, it) }
        val minDist = endStates.minOf { dist[it] ?: Int.MAX_VALUE }
        var currentStates = endStates.filter { dist[it] == minDist }
        while (currentStates.isNotEmpty()) {
            states.addAll(currentStates)
            currentStates = currentStates.flatMap { prev[it] ?: emptyList() }.filter { it !in states }
        }
        val points = states.map { it.position }.toSet()
        return points.size
    }

    val testInput1 = readInput("Day16_test1")
    val testInput2 = readInput("Day16_test2")
    check(part1(testInput1) == 7036)
    check(part1(testInput2) == 11048)
    check(part2(testInput1) == 45)
    check(part2(testInput2) == 64)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
