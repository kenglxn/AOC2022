package aoc2022
fun main() {
    val days = AOC::class.sealedSubclasses
    val solutions = days
        .sortedBy { it.simpleName }
        .map { it.objectInstance as AOC }
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
}

fun String.read(): String = AOC::class.java.getResource("/$this")!!.readText()
