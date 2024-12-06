import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = readInputAsString(name).lines()
fun readInputAsString(name: String) = Path("src/$name.txt").readText().trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> Iterable<T>.countValue(element: T): Int = count { it == element }

fun <T> Iterable<T>.split(predicate: (T) -> Boolean) = buildList<List<T>> {
    var currentList = mutableListOf<T>()
    this@split.forEach {
        if (predicate(it)) {
            add(currentList)
            currentList = mutableListOf<T>()
        } else {
            currentList.add(it)
        }
    }
    if (currentList.isNotEmpty()) {
        add(currentList)
    }
}

fun <T> List<T>.allIndexed(predicate: (Int, T) -> Boolean) = indices.all { predicate(it, this[it]) }

data class Point(val row: Int, val col: Int)
enum class Direction {
    UP, RIGHT, DOWN, LEFT
}
operator fun List<String>.get(point: Point) = getOrNull(point.row)?.getOrNull(point.col)
