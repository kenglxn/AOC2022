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


}

fun String.read() = AOC::class.java.getResource(this)!!.readText()
//fun main(vararg arg: String) {
//    println(AOC.Day3.solve())
//}