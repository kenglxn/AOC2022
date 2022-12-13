package aoc2022

import kotlin.math.absoluteValue

private typealias Coord = Pair<Int, Int>
fun Coord.x() = first
fun Coord.y() = second
operator fun Coord.minus(other: Coord) = (x() - other.x()) to (y() - other.y())
fun Coord.adjacents() = listOf(
    x() to y() + 1,
    x() to y() - 1,
    x() + 1 to y(),
    x() - 1 to y(),
)
// for drawing/rendering o,o == upper left
fun Coord.pathTo(other: Coord) : Day12.Path {
    val (xDiff, yDiff) = other - this
    return when {
        xDiff == 1 && yDiff == 0 -> Day12.Path.RIGHT
        xDiff == -1 && yDiff == 0 -> Day12.Path.LEFT
        xDiff == 0 && yDiff == 1 -> Day12.Path.DOWN
        xDiff == 0 && yDiff == -1 -> Day12.Path.UP
        else -> Day12.Path.NA
    }
}

object Day12 : AOC {
    object Graph {
        var max = 'a'
        lateinit var nodes: Map<Pair<Int, Int>, Vertex>
        lateinit var start: Vertex
        lateinit var end: Vertex

        fun reset() {
            max = 'a'
        }

        operator fun get(it: Pair<Int, Int>) = nodes[it]
    }

    enum class Path(val char: Char) { NA('.'), UP('^'), DOWN('v'), RIGHT('>'), LEFT('<') }
    data class Vertex(
        val coord: Coord,
        val char: Char,
        val adjacents: MutableList<Vertex> = mutableListOf(),
        var path: Path = Path.NA,
        var distance: Long = Long.MAX_VALUE,
    ) {
        val height: Int
            get() = when (char) {
                'S' -> 'a'.code
                'E' -> Graph.max.code
                else -> char.code
            } - 'a'.code

        fun isAdjacent(other: Vertex): Boolean {
            val (xDiff, yDiff) = other.coord - this.coord
            return when {
                xDiff.absoluteValue == 1 && yDiff == 0 -> true
                xDiff == 0 && yDiff.absoluteValue == 1 -> true
                else -> false
            }
        }
    }

    fun parseInput(input: String) {
        Graph.max = 'a'
        Graph.nodes = input.lines().flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val coord = x to y
                val vertex = Vertex(coord, char)
                if (char == 'S') {
                    Graph.start = vertex
                } else if (char == 'E') {
                    Graph.end = vertex
                } else {
                    if (char > Graph.max) Graph.max = char
                }
                coord to vertex
            }
        }.toMap()
    }

    fun bfs(start: Vertex) {
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        start.distance = 0
        while (queue.isNotEmpty()) {
            queue.sortBy { it.distance }
            val current = queue.removeFirst()
            current.adjacents.forEach { v ->
                if (v.distance == Long.MAX_VALUE) {
                    v.distance = current.distance + 1
                    queue.addFirst(v)
                }
            }
        }
    }

    fun dfs(start: Vertex) {
        start.distance = 0
        val queue = Graph.nodes.values.toMutableList()

        while (queue.isNotEmpty()) {
            queue.sortBy { it.distance }
            val current = queue.removeFirst()
            current.adjacents.forEach { v ->
                if (v in queue) {
                    var newDistance = Long.MAX_VALUE
                    if (current.distance != Long.MAX_VALUE) {
                        newDistance = current.distance + 1
                    }
                    if (newDistance < v.distance) {
                        v.distance = newDistance
                    }
                }
            }
        }
    }

    fun walkGraph(start: Vertex = Graph.start, dfs: Boolean = true) {
        val walkingBack = start == Graph.end
        Graph.nodes.values.map { vertex ->
            vertex.distance = Long.MAX_VALUE
            vertex.adjacents.clear()
            vertex.coord
                .adjacents()
                .forEach {
                    val other = Graph[it]
                    if (other != null && vertex.isAdjacent(other)) {
                        if (walkingBack) {
                            if (vertex.height <= other.height + 1) {
                                vertex.adjacents.add(other)
                            }
                        } else {
                            if (other.height <= vertex.height + 1) {
                                vertex.adjacents.add(other)
                            }
                        }
                    }
                }
        }

        if (dfs) dfs(start) else bfs(start)
    }

    override fun solve(): String {
        val input = "day12.input".read()
        parseInput(input)

        walkGraph()
        val part1 = Graph.end.distance

        walkGraph(start = Graph.end)
        val ground = Graph.nodes.values.filter { it.char == 'a' }
        val part2 = ground.map { it.distance }.min()
        return """
            |## Day 12
            |* Part 1: 
            | * $part1
            |* Part 2: 
            | * $part2
        """.trimMargin()
    }
}

fun main() {
    print(Day12.solve())
}
