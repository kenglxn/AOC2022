package aoc2022

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