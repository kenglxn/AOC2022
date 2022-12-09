fun main() {
    val solutions = AOC::class.nestedClasses
        .map { it.objectInstance as AOC }
        .sortedBy { it.javaClass.simpleName }
        .map { it.solve() }
        .joinToString("\n")
    """# 🎅🏻Advent Of Code 2022 🎄
      | $solutions
    """.trimMargin().also {
        println(it)
        //File("./README.md").writeText(it)
    }
}

sealed interface AOC {
    fun solve(): String

    object Day1 : AOC {
        override fun solve(): String {
            val elves = "day1.input".read().split("\n\n")
                .mapIndexed { index, s -> index to s.split("\n").map(String::toInt).sum() }
                .sortedByDescending { (idx, cal) -> cal }

            return """
                |## Day 1
                |* Part 1: 
                |  * Elf:${elves.first().first} = kCal:${elves.first().second}
                |* Part 2:
                |  * ${elves.take(3).sumOf { it.second }}
            """.trimMargin()
        }
    }

    object Day2 : AOC {
        enum class Outcome(val score: Int, val code: String) { WIN(6, "Z"), DRAW(3, "Y"), LOSE(0, "X") }

        val victors = mapOf(
            Tool.ROCK to Tool.PAPER,
            Tool.PAPER to Tool.SCISSORS,
            Tool.SCISSORS to Tool.ROCK,
        )

        enum class Tool(val theirCode: String, val ourCode: String, val score: Int) {
            ROCK("A", "X", 1),
            PAPER("B", "Y", 2),
            SCISSORS("C", "Z", 3);

            fun against(other: Tool): Outcome = when {
                this == other -> Outcome.DRAW
                victors[other] == this -> Outcome.WIN
                else -> Outcome.LOSE
            }

            fun fromOutcome(code: String): Tool {
                val outcome = Outcome.values().find { it.code == code }!!
                return when (outcome) {
                    Outcome.LOSE -> victors.entries.find { (k, v) -> v == this }!!.key
                    Outcome.WIN -> victors[this]!!
                    Outcome.DRAW -> this
                }
            }

            companion object {
                fun score(them: String, us: String): Int {
                    return values().find { it.ourCode == us }!!.let { our ->
                        our.against(values().find { it.theirCode == them }!!).score + our.score
                    }
                }
            }
        }

        fun String.us(): Tool = Tool.values().find { it.ourCode == this }!!
        fun String.them(): Tool = Tool.values().find { it.theirCode == this }!!

        override fun solve(): String {
            val part1 = "day2.input".read()
                .split("\n")
                .map { line -> line.split(" ") }
                .map { (left, right) ->
                    right.us().let { us ->
                        val outcome = us.against(left.them())
                        listOf(us, left.them(), outcome, us.score + outcome.score)
                    }
                }

            // x = lose, y = draw, z = win
            val part2 = "day2.input".read()
                .split("\n")
                .map { line -> line.split(" ") }
                .map { (left, right) ->
                    left.them().let {
                        val wanted = Outcome.values().find { it.code == right }
                        val us = it.fromOutcome(right)
                        val outcome = us.against(left.them())
                        listOf(wanted, us, left.them(), outcome, us.score + outcome.score)
                    }
                }

            return """
                |## Day 2
                |* Part 1: 
                | * ${part1.sumOf { it.last() as Int }}
                |* Part 2: 
                | * ${part2.sumOf { it.last() as Int }}
            """.trimMargin()
        }
    }

    object Day3 : AOC {
        val priorities = (('a'..'z') + ('A'..'Z'))
            .mapIndexed { index, char -> char to index + 1 }
            .toMap()

        override fun solve(): String {
            val rucksacks = "day3.input".read().split("\n")

            val duplicates = rucksacks.map {
                val (first, second) = it.chunked(it.length / 2)
                first.toSet() intersect second.toSet()
            }

            val groups = rucksacks.chunked(3).map { group ->
                group[0].toSet() intersect group[1].toSet() intersect group[2].toSet()
            }

            return """
                |## Day 3
                |* Part 1: 
                | * ${duplicates.flatten().sumOf { priorities[it] as Int }}
                |* Part 2: 
                | * ${groups.flatten().sumOf { priorities[it] as Int }}
            """.trimMargin()
        }
    }

    object Day4 : AOC {
        private fun String.range(): IntRange = split('-').first().toInt()..split('-').last().toInt()
        operator fun String.component1() = split(",").first()
        operator fun String.component2() = split(",").last()

        override fun solve(): String {
            val elves = "day4.input".read().split("\n")
                .map { (first, last) -> first.range() to last.range() }

            val completeOverlaps = elves.count { (first, last) ->
                (first - last).isEmpty() || (last - first).isEmpty()
            }

            val anyOverlap = elves.count { (first, last) ->
                (first intersect last).isNotEmpty()
            }

            return """
                |## Day 4
                |* Part 1: 
                | * $completeOverlaps
                |* Part 2: 
                | * $anyOverlap
            """.trimMargin()
        }
    }

    object Day5 : AOC {
        operator fun <T> Array<T>.component6() = this[5]
        operator fun <T> Array<T>.component7() = this[6]
        operator fun <T> Array<T>.component8() = this[7]
        operator fun <T> Array<T>.component9() = this[8]
        operator fun String.component1() = at(1)
        operator fun String.component2() = at(5)
        operator fun String.component3() = at(9)
        operator fun String.component4() = at(13)
        operator fun String.component5() = at(17)
        operator fun String.component6() = at(21)
        operator fun String.component7() = at(25)
        operator fun String.component8() = at(29)
        operator fun String.component9() = at(33)
        operator fun Array<Array<String>>.component1() = map { (i, _, _, _, _, _, _, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component2() = map { (_, i, _, _, _, _, _, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component3() = map { (_, _, i, _, _, _, _, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component4() = map { (_, _, _, i, _, _, _, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component5() = map { (_, _, _, _, i, _, _, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component6() = map { (_, _, _, _, _, i, _, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component7() = map { (_, _, _, _, _, _, i, _, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component8() = map { (_, _, _, _, _, _, _, i, _) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component9() = map { (_, _, _, _, _, _, _, _, i) -> i }.filter { it.isNotBlank() }.toTypedArray()
        fun String.at(i: Int) = try { this[i].toString() } catch (e: Exception) { "" }

        fun MutableList<String>.takeAndRemove(amount: Int): List<String> {
            val taken = take(amount)
            repeat(amount) { removeFirstOrNull() }
            return taken
        }

        fun String.instruct(): Triple<Int, Int, Int> {
            val (a, b, c) = Regex("move (\\d+) from (\\d+) to (\\d+)").find(this)!!.destructured
            return Triple(a.toInt(), b.toInt(), c.toInt())
        }

        override fun solve(): String {
            val (stack1, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9) = "day5.input".read()
                .split("\n")
                .filter { it.contains('[') }
                .map { (a, b, c, d, e, f, g, h, i) -> arrayOf(a, b, c, d, e, f, g, h, i) }
                .toTypedArray()

            val stacks9000 = arrayOf(
                stack1.toMutableList(),
                stack2.toMutableList(),
                stack3.toMutableList(),
                stack4.toMutableList(),
                stack5.toMutableList(),
                stack6.toMutableList(),
                stack7.toMutableList(),
                stack8.toMutableList(),
                stack9.toMutableList(),
            )
            val stacks9001 = stacks9000.map { it.toMutableList() }

            val instructions = "day5.input".read().split("\n").filter { it.contains("move") }
            instructions.forEach {
                val (amount, source, destination) = it.instruct()
                val taken = stacks9000[source - 1].takeAndRemove(amount)
                stacks9000[destination - 1].addAll(0, taken.reversed())
            }
            instructions.forEach {
                val (amount, source, destination) = it.instruct()
                val taken = stacks9001[source - 1].takeAndRemove(amount)
                stacks9001[destination - 1].addAll(0, taken)
            }

            return """
                |## Day 5
                |* Part 1: 
                | * ${stacks9000.map { it.firstOrNull() }.joinToString("")}
                |* Part 2: 
                | * ${stacks9001.map { it.firstOrNull() }.joinToString("")}
            """.trimMargin()
        }
    }

    object Day6 : AOC {
        fun String.markerWithSize(markerSize: Int) =
            this.withIndex()
                .windowed(markerSize)
                .find {
                    it.map { v -> v.value }.toSet().size == markerSize
                }!!.last().index + 1

        fun String.startOfPacket() = markerWithSize(4)
        fun String.startOfMessage() = markerWithSize(14)

        override fun solve(): String {
            val input = "day6.input".read()
            val packetMarker = input.startOfPacket()
            val messageMarker = input.startOfMessage()

            return """
                |## Day 6
                |* Part 1: 
                | * $packetMarker
                |* Part 2: 
                | * $messageMarker
            """.trimMargin()
        }
    }

    object Day7 : AOC {
        private fun String.cmd() = when {
            startsWith("\$ cd") -> CMD.CD
            startsWith("\$ ls") -> CMD.LS
            else -> null
        }
        enum class CMD { CD, LS }
        object FS {
            val size = 70000000
            val root = Dir("/", parent = null)
            fun free() = size - root.size()
            fun print() = root.print()
        }

        data class File(val name: String, val size: Int, val parent: Dir) {
            private val level: Int = parent.level.plus(1)
            override fun toString(): String = "$name (file, size=$size)"
            fun print() {
                repeat(level) {
                    print("\t")
                }
                print("- $this\n")
            }
        }

        data class Dir(val name: String, val parent: Dir?, val files: MutableList<File> = mutableListOf(), val dirs: MutableList<Dir> = mutableListOf()) {
            val level: Int = parent?.level?.plus(1) ?: 0
            override fun toString(): String = "$name (dir, size=${size()})"
            fun size() : Int = files.sumOf { it.size } + dirs.sumOf { it.size() }
            fun rDirs() : List<Dir> = dirs + dirs.flatMap { it.rDirs() }

            fun print() {
                repeat(level) {
                    print("\t")
                }
                print("- $this\n")
                dirs.forEach(Dir::print)
                files.forEach(File::print)
            }
        }

        override fun solve(): String {
            var cwd = FS.root
            "day7.input".read().lines()
                .filter { it.isNotEmpty() }
                .forEachIndexed { _, it ->
                    when (it.cmd()) {
                        CMD.CD -> {
                            val dirName = it.split(" ").last()
                            if (dirName == "..") {
                                cwd = cwd.parent ?: FS.root
                            } else if (dirName == "/") {
                                cwd = FS.root
                            } else {
                                if (dirName != cwd.name) {
                                    cwd = cwd.dirs.find { d -> d.name == dirName }
                                        ?: Dir(name = dirName, parent = cwd).apply {
                                            cwd.dirs.add(this)
                                        }
                                }
                            }
                        }
                        CMD.LS -> Unit // noop, contents next
                        null -> { // contents
                            if (it.startsWith("dir")) {
                                val dirName = it.split(" ").last()
                                cwd.dirs.add(Dir(name = dirName, parent = cwd))
                            } else {
                                val (size, name) = it.split(" ")
                                cwd.files.add(File(name = name, size = size.toInt(), parent = cwd))
                            }
                        }
                    }
                }

            // FS.print() // debug
            val minFree = 30000000
            val need = minFree - FS.free()
            return """
                |## Day 7
                |* Part 1: 
                | * ${FS.root.rDirs().filter { it.size() < 100000 }.sumOf { it.size() }}
                |* Part 2: 
                | * ${FS.root.rDirs().filter { it.size() > need }.minByOrNull { it.size() }}
            """.trimMargin()
        }
    }

    object Day8 : AOC {
        data class Forest(val height: Int, val width: Int, val trees: List<Tree>) {
            init {
                trees.forEach {
                    it.forest = this
                }
            }
            fun above(tree: Tree): List<Tree> = trees.filter { it.above(tree) }.sortedBy { it.y }
            fun below(tree: Tree): List<Tree> = trees.filter { it.below(tree) }.sortedByDescending { it.y }
            fun left(tree: Tree): List<Tree> = trees.filter { it.left(tree) }.sortedByDescending { it.x }
            fun right(tree: Tree): List<Tree> = trees.filter { it.right(tree) }.sortedBy { it.x }
        }

        data class Tree(val height: Int, val position: Pair<Int, Int>) {
            val x = position.first
            val y = position.second
            lateinit var forest: Forest
            fun above(other: Tree): Boolean = (x == other.x && y > other.y)
            fun above() = forest.above(this)
            fun below(other: Tree): Boolean = (x == other.x && y < other.y)
            fun below() = forest.below(this)
            fun left(other: Tree): Boolean = (x < other.x && y == other.y)
            fun left() = forest.left(this)
            fun right(other: Tree): Boolean = (x > other.x && y == other.y)
            fun right() = forest.right(this)
            fun visibleTop() = above().all { it < this }
            fun visibleLeft() = left().all { it < this }
            fun visibleRight() = right().all { it < this }
            fun visibleDown() = below().all { it < this }
            fun visible() = visibleTop() || visibleLeft() || visibleRight() || visibleDown()
            fun viewDistanceAbove() = above().let {
                minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
            }
            fun viewDistanceBelow() = below().let {
                minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
            }
            fun viewDistanceLeft() = left().let {
                minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
            }
            fun viewDistanceRight() = right().let {
                minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
            }
            fun scenicScore() = viewDistanceAbove() * viewDistanceLeft() * viewDistanceRight() * viewDistanceBelow()
            operator fun compareTo(tree: Tree) = height.compareTo(tree.height)
        }

        override fun solve(): String {
            val lines = "day8.input".read().lines().reversed()
            val trees = lines.flatMapIndexed { y, row ->
                row.mapIndexed { x, cell ->
                    Tree(cell.digitToInt(), x to y)
                }
            }
            val forest = Forest(lines.size, lines.first().length, trees)
            val visible = forest.trees.filter(Tree::visible)
            val best = forest.trees.maxBy(Tree::scenicScore)
            return """
                |## Day 8
                |* Part 1: 
                | * ${visible.size}
                |* Part 2: 
                | * ${best.scenicScore()}
            """.trimMargin()
        }
    }

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
            // part2Rope.print() // debug
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
}

fun String.read() = AOC::class.java.getResource(this)!!.readText()
fun main(vararg arg: String) {
    println(AOC.Day9.solve())
}

fun AOC.Day9.Rope.print(printSteps: Boolean = false) {
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