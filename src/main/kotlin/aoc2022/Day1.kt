package aoc2022

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