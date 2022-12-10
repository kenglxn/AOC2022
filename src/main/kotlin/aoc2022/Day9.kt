package aoc2022

object Day9 : AOC {
    enum class Direction { R, U, L, D }
    data class Knot(val x: Int, val y: Int, val pos: Int) {
        fun move(dir: Direction) = when (dir) {
            Direction.R -> copy(x = x + 1)
            Direction.U -> copy(y = y + 1)
            Direction.L -> copy(x = x - 1)
            Direction.D -> copy(y = y - 1)
        }
        fun follow(head: Knot) : Knot {
            val diff = head - this
            return when {
                diff.x > 1 -> copy(x = x + 1, y = y + diff.y.shift())
                diff.x < -1 -> copy(x = x - 1, y = y + diff.y.shift())
                diff.y > 1 -> copy(x = x + diff.x.shift(), y = y + 1)
                diff.y < -1 -> copy(x = x + diff.x.shift(), y = y - 1)
                else -> copy()
            }
        }
        fun Int.shift() : Int {
            return when {
                this >= 1 -> 1
                this <= -1 -> -1
                else -> 0
            }
        }
        operator fun minus(knot: Knot) = Knot(x - knot.x, y - knot.y, pos)
    }
    class Rope(val knots: MutableList<Knot>) {
        companion object {
            fun of(size: Int)= Rope((0 until size).map { Knot(0, 0, it) }.toMutableList())
        }
        val states = mutableListOf<Pair<List<Knot>, Pair<Direction, Int>?>>(knots.toList() to null)

        fun move(dir: Direction, steps: Int) {
            repeat(steps) {
                knots.forEach { knot ->
                    if (knot.pos == 0) {
                        knots[0] = knot.move(dir)
                    } else {
                        knots[knot.pos] = knot.follow(knots[knot.pos - 1])
                    }
                }

                states.add(knots.toList() to (dir to steps))
            }
        }
    }
    fun String.directions() : Pair<Direction, Int> {
        val (dir, steps) = split(" ")
        return Direction.valueOf(dir) to steps.toInt()
    }
    override fun solve(): String {
        val part1Rope = Rope.of(2)
        val part2Rope = Rope.of(10)
        val directions = "day9.input".read().lines().map  { it.directions() }
        directions.forEach { (dir, steps) ->
            part1Rope.move(dir, steps)
            part2Rope.move(dir, steps)
        }
        // part2Rope.aoc2022.print() // debug
        val tails1 = part1Rope.states.map { it.first.last().copy(pos = 0) }.toSet()
        val tails2 = part2Rope.states.map { it.first.last().copy(pos = 0) }.toSet()
        return """
            |## Day 9
            |* Part 1: 
            | * ${tails1.size}
            |* Part 2: 
            | * ${tails2.size}
        """.trimMargin()
    }
}

fun Day9.Rope.print(printSteps: Boolean = false) {
    states.distinctBy {
        if (printSteps) it else it.second
    }.forEachIndexed { index, (state, directions) ->
        val maxX = state.maxBy { it.x }.x + 3
        val maxY = state.maxBy { it.y }.y + 3
        val minX =  state.minBy { it.x }.x - 3
        val minY =  state.minBy { it.y }.y - 3
        println("== ${directions?.first} ${directions?.second} == idx:$index")
        (minY..maxY).reversed().forEach { y ->
            (minX..maxX).forEach { x ->
                val char = state.find { knot ->
                    knot.x == x && knot.y == y
                }?.let { knot ->
                    if (knot.pos == 0) {
                        "H"
                    } else {
                        "${knot.pos}"
                    }
                } ?: "."
                print(char)
            }
            println()
        }
        println()
    }
}