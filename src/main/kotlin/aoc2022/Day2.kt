package aoc2022

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