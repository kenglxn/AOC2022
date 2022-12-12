package aoc2022

object Day11 : AOC {
    val applyRelief = { worry: Long -> (worry / 3) }
    val monkeyMap = mutableMapOf<Int, Monkey>()
    fun monkey(nr: Int) = monkeyMap[nr]!!
    data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: (old: Long) -> Long,
        val test: (item: Long) -> Boolean,
        val divisor: Long,
        val targets: Map<Boolean, Int>,
        var tests: Long = 0,
    ) {
        companion object {
            val regex = Regex("Monkey (\\d+):")
            fun parse(str: String) : Monkey {
                val (monkeyId) = Regex("Monkey (\\d+):").find(str)!!.destructured
                val (itemsStr) = Regex("Starting items: ((?:\\d+(?:, )?)*)").find(str)!!.destructured
                val items = itemsStr.split(", ").map { it.toLong() }
                val (operator, operandStr) = Regex("Operation: new = old (.+) (.+)").find(str)!!.destructured
                val (divisor) = Regex("Test: divisible by (\\d+)").find(str)!!.destructured
                val (trueTarget) = Regex("If true: throw to monkey (\\d+)").find(str)!!.destructured
                val (falseTarget) = Regex("If false: throw to monkey (\\d+)").find(str)!!.destructured

                return Monkey(
                    id = monkeyId.toInt(),
                    items = items.toMutableList(),
                    operation = { old ->
                        val operand = when(operandStr) {
                            "old" -> old
                            else -> operandStr.toLong()
                        }
                        when (operator) {
                            "*" -> old * operand
                            "+" -> old + operand
                            "-" -> old - operand
                            "/" -> old / operand
                            else -> throw Error("unsupported operator $operator")
                        }
                    },
                    divisor = divisor.toLong(),
                    test = { item -> item % divisor.toLong() == 0L },
                    targets = mapOf(
                        true to trueTarget.toInt(),
                        false to falseTarget.toInt(),
                    ),
                )
            }
        }

        fun seeDo(reliefFn : (Long) -> Long) {
            items.forEach { item ->
                val worry = operation(item)
                val newWorry = reliefFn(worry)
                val b = test(newWorry)
                tests += 1
                monkey(targets[b]!!).items.add(newWorry)
            }
            items.clear()
        }
    }

    fun playRound(reliefFn : (Long) -> Long) {
        monkeyMap.toSortedMap().values.forEach { monkey ->
            monkey.seeDo(reliefFn)
        }
    }
    fun play(rounds: Int, reliefFn : (Long) -> Long) = repeat(rounds) { playRound(reliefFn) }
    fun monkeyBusiness() = monkeyMap.values.map { it.tests }.sortedDescending().take(2).reduce(Long::times)

    fun parseInput(input: String) {
        monkeyMap.clear()
        input.split(Regex("\n\n")).forEach { monkeyStr ->
            Monkey.parse(monkeyStr).also { monkeyMap[it.id] = it }
        }
    }

    override fun solve(): String {
        val input = "day11.input".read()
        parseInput(input)
        play(20) { it / 3 }
        val part1 = monkeyBusiness()
        parseInput(input)
        play(10000) { it % (monkeyMap.values.map { it.divisor }.reduce(Long::times)) }
        val part2 = monkeyBusiness()

        return """
            |## Day 11
            |* Part 1: 
            | * $part1
            |* Part 2: 
            | * $part2
        """.trimMargin()
    }
}

fun main() {
    print(Day11.solve())
}