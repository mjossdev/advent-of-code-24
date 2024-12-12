fun main() {
    data class Region(val plant: Char, val points: Set<Point>)
    fun Region.area() = points.size
    fun Region.perimeter() = points.sumOf { point ->
        point.neighbors().count { it !in points }
    }
    fun Region.sides(): Int {
        return 0
    }

    fun readRegions(input: List<String>): List<Region> {
        val pointsByPlant = input.points().groupBy { input[it.row][it.col] }
        val regionsByPlant = buildMap<Char, MutableList<MutableSet<Point>>> {
            pointsByPlant.forEach { (plant, points) ->
                points.forEach { point ->
                    val neighbors = point.neighbors()
                    val areas = this.computeIfAbsent(plant) { mutableListOf() }
                    var set = areas.find { area -> neighbors.any { it in area } }
                    if (set == null) {
                        set = mutableSetOf()
                        areas.add(set)
                    }
                    set.add(point)
                }
            }
            values.forEach { areas ->
                areas.forEach { area ->
                    areas.filter { other -> other != area && other.any { point -> point.neighbors().any { it in area } } }
                        .forEach { other ->
                            other.addAll(area)
                            area.addAll(other)
                        }
                }
            }
        }.mapValues { it.value.distinct() }
        return regionsByPlant.flatMap { (plant, areas) ->
            areas.map {
                Region(plant, it)
            }
        }
    }

    fun part1(input: List<String>): Int = readRegions(input).sumOf {
        it.area() * it.perimeter()
    }

    fun part2(input: List<String>): Int = readRegions(input).sumOf {
        println("${it.plant} ${it.sides()}")
        it.area() * it.sides()
    }

    val testInput1 = readInput("Day12_test1")
    val testInput2 = readInput("Day12_test2")
    val testInput3 = readInput("Day12_test3")
    val testInput4 = readInput("Day12_test4")
    val testInput5 = readInput("Day12_test5")
    check(part1(testInput1) == 140)
    check(part1(testInput2) == 772)
    check(part1(testInput3) == 1930)
//    check(part2(testInput1) == 80)
//    check(part2(testInput2) == 436)
//    check(part2(testInput3) == 1206)
//    check(part2(testInput4) == 236)
//    check(part2(testInput5) == 368)


    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
