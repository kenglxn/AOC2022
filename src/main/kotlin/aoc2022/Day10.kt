package aoc2022

import kotlin.properties.Delegates

object Day10 : AOC {
    enum class Pixel(val char: Char) {
        LIT('#'),
        DARK('.');

        override fun toString() = char.toString()
    }
    object CRT {
        val pixmap = mutableMapOf<Int, Pixel>()
        fun reset() = pixmap.clear()

        fun draw(cycle: Int, register: Int) {
            val spritePositions = listOf(register-1, register, register+1)
            pixmap[cycle - 1] =
                if (spritePositions.contains((cycle - 1) % 40))
                    Pixel.LIT
                else
                    Pixel.DARK
        }
        fun render(): String {
            val pixels = pixmap.entries.sortedBy { it.key }.map { it.value }
            return pixels.joinToString("").chunked(40).joinToString("\n")
        }
    }
    object CPU {
        var cycles: Int by Delegates.observable(0) { property, oldValue, newValue ->
            if (newValue == 0) {
                CRT.reset()
            } else {
                CRT.draw(newValue, register.sum())
            }
        }
        val register = mutableListOf(1)
        val signalStrengths = mutableListOf<SignalStrength>()

        fun reset() {
            signalStrengths.clear()
            register.clear()
            register.add(1)
            cycles = 0
        }

        fun registerSignal() = signalStrengths.add(cycles to register.sum())

        fun process(instr: Instruction) {
            repeat(instr.cmd.cycles) {
                cycles += 1
                if (cycles.isSignal()) {
                    registerSignal()
                }
            }
            if (instr.cmd == CMD.ADDX) {
                register.add(instr.operand)
            }
        }

        private infix fun Int.to(register: Int) = SignalStrength(this, register)
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
        val render = CRT.render()
        return """
            |## Day 10
            |* Part 1: 
            | * $strength
            |* Part 2: 
            |```
            |$render
            |```
        """.trimMargin()
    }
}

fun Int.isSignal() = (this - 20) % 40 == 0

fun main() {
    print(Day10.solve())
}