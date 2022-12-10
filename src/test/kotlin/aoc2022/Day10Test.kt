package aoc2022

import aoc2022.Day10.SignalStrength
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe

class Day10Test : DescribeSpec({

    describe("signal strength cycle") {
        withData(listOf(
            0 to false,
            7 to false,
            10 to false,
            20 to true,
            39 to false,
            40 to false,
            60 to true,
            100 to true,
            140 to true,
            180 to true,
            220 to true,
        )) { (cycle, bool) ->
            cycle.isSignal() shouldBe bool
        }
    }

    describe("cycle count and instruction register") {

        Day10.processInput(
            """
                noop
                addx 3
                addx -5
            """.trimIndent()
        )
        Day10.CPU.cycles shouldBe 5
        Day10.CPU.register.sum() shouldBe -1
        Day10.CPU.signalStrengths should beEmpty()
    }

    describe("process input") {
        Day10.processInput(input)
        Day10.CPU.signalStrengths.sumOf { it.strenght } shouldBe 13140
        Day10.CPU.signalStrengths shouldBe mutableListOf(
            SignalStrength(20, 21), // 420
            SignalStrength(60, 19), // 1140
            SignalStrength(100, 18), // 1800
            SignalStrength(140, 21), // 2940
            SignalStrength(180, 16), // 2880
            SignalStrength(220, 18), // 3960
        )
    }

    describe("CRT") {
        Day10.processInput(input)
        Day10.CRT.render().lines().first() shouldBe "##..##..##..##..##..##..##..##..##..##.."
        Day10.CRT.render() shouldBe """
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....
        """.trimIndent()
    }
})

private val input = """
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
""".trimIndent()