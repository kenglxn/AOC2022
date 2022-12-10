package aoc2022

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