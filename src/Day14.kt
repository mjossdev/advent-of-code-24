fun main() {
    data class Velocity(val x: Int, val y: Int)
    data class Robot(val position: Coordinate, val velocity: Velocity)

    val inputRegex = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""")
    fun String.toRobot(): Robot {
        val matchResult = inputRegex.matchEntire(this) ?: error("Invalid robot: $this")
        val (px, py, vx, vy) = matchResult.groupValues.drop(1).map { it.toInt() }
        return Robot(
            Coordinate(px, py),
            Velocity(vx, vy)
        )
    }

    fun part1(input: List<String>, width: Int, height: Int): Int {
        val robots = input.map { it.toRobot() }
        val seconds = 100
        var tl = 0
        var tr = 0
        var bl = 0
        var br = 0
        val wm = width / 2
        val hm = height / 2
        for (robot in robots) {
            val x = ((robot.position.x + robot.velocity.x * seconds) % width).let { if (it < 0) it + width else it }
            val y = ((robot.position.y + robot.velocity.y * seconds) % height).let { if (it < 0) it + height else it }
            when {
                x < wm -> {
                    when {
                        y < hm -> {
                            ++tl
                        }

                        y > hm -> {
                            ++bl
                        }
                    }
                }

                x > wm -> {
                    when {
                        y < hm -> {
                            ++tr
                        }

                        y > hm -> {
                            ++br
                        }
                    }
                }
            }
        }
        return tl * tr * bl * br
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput, 11, 7) == 12)
//    check(part2(testInput) == 81)

    val input = readInput("Day14")
    part1(input, 101, 103).println()
//    part2(input).println()
}
