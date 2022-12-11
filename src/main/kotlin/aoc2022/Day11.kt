package aoc2022

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import java.lang.System.currentTimeMillis

object Day11 : AOC {
    val applyRelief = { worry: BigInteger -> (worry / 3) }
    val monkeyMap = mutableMapOf<Int, Monkey>()
    fun monkey(nr: Int) = monkeyMap[nr]!!
    data class Monkey(
        val id: Int,
        val items: MutableList<BigInteger>,
        val operation: (old: BigInteger) -> BigInteger,
        val test: (item: BigInteger) -> Boolean,
        val targets: Map<Boolean, Int>,
        var tests: Int = 0,
    ) {
        companion object {
            val regex = Regex("Monkey (\\d+):")
            fun parse(str: String) : Monkey {
                val (monkeyId) = Regex("Monkey (\\d+):").find(str)!!.destructured
                val (itemsStr) = Regex("Starting items: ((?:\\d+(?:, )?)*)").find(str)!!.destructured
                val items = itemsStr.split(", ").map { it.toBigInteger() }
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
                            else -> operandStr.toBigInteger()
                        }
                        when (operator) {
                            "*" -> old * operand
                            "+" -> old + operand
                            "-" -> old - operand
                            "/" -> old / operand
                            else -> throw Error("unsupported operator $operator")
                        }
                    },
                    test = { item -> item % divisor.toBigInteger() == 0.toBigInteger() },
                    targets = mapOf(
                        true to trueTarget.toInt(),
                        false to falseTarget.toInt(),
                    ),
                )
            }
        }

        fun seeDo(applyRelief : Boolean) {
            items.forEach { item ->
                val worry = operation(item)
                val newWorry = if (applyRelief) applyRelief(worry) else worry
                val b = test(newWorry)
                tests += 1
                monkey(targets[b]!!).items.add(newWorry)
            }
            items.clear()
        }
    }

    fun playRound(applyRelief : Boolean = true) {
        monkeyMap.toSortedMap().values.forEach { monkey ->
            monkey.seeDo(applyRelief)
        }
    }
    fun play(rounds: Int, applyRelief : Boolean = true) = repeat(rounds) {
        val start = currentTimeMillis()
        playRound(applyRelief)
        println("round $it took ${currentTimeMillis() - start} ms")
    }
    fun monkeyBusiness() = monkeyMap.values.map { it.tests }.sortedDescending().take(2).reduce(Int::times)

    fun parseInput(input: String) {
        monkeyMap.clear()
        input.split(Regex("\n\n")).forEach { monkeyStr ->
            Monkey.parse(monkeyStr).also { monkeyMap[it.id] = it }
        }
    }

    override fun solve(): String {
        val input = "day11.input".read()
        parseInput(input)
        play(20)
        val part1 = monkeyBusiness()

        parseInput(input)
        play(10000, false)
        val part2 = monkeyBusiness()

        return """
            |## Day 11
            |* Part 1: 
            | * $part1
            |* Part 2: 
            | *
        """.trimMargin()
    }
}

fun main() {
    print(Day11.solve())
}