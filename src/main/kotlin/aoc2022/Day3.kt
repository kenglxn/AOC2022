package aoc2022

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