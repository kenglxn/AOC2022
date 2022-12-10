package aoc2022

import java.util.concurrent.atomic.AtomicInteger

object Day10 : AOC {
    object CPU {
        val cycles = AtomicInteger(0)
        val register = mutableListOf(1)
        val signalStrengths = mutableListOf<SignalStrength>()

        fun reset() {
            signalStrengths.clear()
            register.clear()
            register.add(1)
            cycles.set(0)
        }

        fun registerSignal() = signalStrengths.add(cycles to register.sum())

        fun process(instr: Instruction) {
            repeat(instr.cmd.cycles) {
                if (cycles.incrementAndGet().isSignal()) {
                    registerSignal()
                }
            }
            if (instr.cmd == CMD.ADDX) {
                register.add(instr.operand)
            }
        }

        private infix fun AtomicInteger.to(register: Int) = SignalStrength(get(), register)
    }

    data class SignalStrength(val cycle: Int, val register: Int) {
        val strenght = cycle * register
    }
    enum class CMD(val cycles: Int) { NOOP(1), ADDX(2) }
    fun String.asCMD() = if(this == "noop") CMD.NOOP else CMD.ADDX
    class Instruction(input: String) {
        val cmd = input.asCMD()
        val operand = if(cmd == CMD.ADDX) input.split(" ").last().toInt() else 0

        operator fun component1() = cmd
        operator fun component2() = operand
    }

    fun processInput(input: String) {
        CPU.reset()
        input.lines()
            .map(::Instruction)
            .forEach(CPU::process)
    }

    override fun solve(): String {
        processInput("day10.input".read())
        val strength = CPU.signalStrengths.sumOf { it.strenght }
        return """
            |## Day 10
            |* Part 1: 
            | * $strength
            |* Part 2: 
            | * 
        """.trimMargin()
    }
}

fun Int.isSignal() = (this - 20) % 40 == 0

fun main() {
    print(Day10.solve())
}