import java.io.File

fun main() {
    val solutions = AOC::class.nestedClasses
        .map { it.objectInstance as AOC }
        .sortedBy { it.javaClass.simpleName }
        .map { it.solve() }
        .joinToString("")
    """# 🎅🏻Advent Of Code 2022 🎄
      | $solutions
    """.trimMargin().printAndWrite()
}

sealed interface AOC {
    fun solve(): String

    object Day1 : AOC {
        override fun solve() : String {
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
}

fun String.read() = AOC::class.java.getResource(this)!!.readText()

fun String.printAndWrite() {
    println(this)
    File("./README.md").writeText(this)
}