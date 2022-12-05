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
                    Outcome.LOSE -> victors.entries.find { (k,v) -> v == this }!!.key
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
        operator fun String.component9()= at(33)
        fun String.at(i: Int): String {
            return try {
                this[i].toString()
            } catch (e: Exception) {
                ""
            }
        }

        operator fun Array<Array<String>>.component1() = map { (i,_,_,_,_,_,_,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component2() = map { (_,i,_,_,_,_,_,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component3() = map { (_,_,i,_,_,_,_,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component4() = map { (_,_,_,i,_,_,_,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component5() = map { (_,_,_,_,i,_,_,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component6() = map { (_,_,_,_,_,i,_,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component7() = map { (_,_,_,_,_,_,i,_,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component8() = map { (_,_,_,_,_,_,_,i,_) -> i }.filter { it.isNotBlank() }.toTypedArray()
        operator fun Array<Array<String>>.component9() = map { (_,_,_,_,_,_,_,_,i) -> i }.filter { it.isNotBlank() }.toTypedArray()
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
            val (stack1, stack2, stack3, stack4, stack5, stack6, stack7, stack8, stack9) = "day5.input".read().split("\n")
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
}

fun String.read() = AOC::class.java.getResource(this)!!.readText()
//fun main(vararg arg: String) {
//    println(AOC.Day5.solve())
//}