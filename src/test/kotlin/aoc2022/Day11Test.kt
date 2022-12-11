package aoc2022

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class Day11Test : DescribeSpec({

    describe("Monkeys") {
        Day11.parseInput(example)
        Day11.monkey(0).items shouldBe listOf(79, 98)
        Day11.monkey(0).operation(1) shouldBe (1 * 19)
        Day11.monkey(0).operation(2) shouldBe (2 * 19)
        Day11.monkey(0).test(23) shouldBe true
        Day11.monkey(0).test((23 * 2)) shouldBe true
        Day11.monkey(0).test(24) shouldBe false
        Day11.monkey(0).targets[true] shouldBe 2
        Day11.monkey(0).targets[false] shouldBe 3

        Day11.monkey(1).items shouldBe listOf(54, 65, 75, 74)
        Day11.monkey(1).operation(1) shouldBe 1 + 6
        Day11.monkey(1).operation(2) shouldBe 2 + 6
        Day11.monkey(1).test(19) shouldBe true
        Day11.monkey(1).test(19 * 2) shouldBe true
        Day11.monkey(1).test(20) shouldBe false
        Day11.monkey(1).targets[true] shouldBe 2
        Day11.monkey(1).targets[false] shouldBe 0

        Day11.monkey(2).items shouldBe listOf(79, 60, 97)
        Day11.monkey(2).operation(1) shouldBe 1 * 1
        Day11.monkey(2).operation(2) shouldBe 2 * 2
        Day11.monkey(2).test(13) shouldBe true
        Day11.monkey(2).test(13 * 2) shouldBe true
        Day11.monkey(2).test(14) shouldBe false
        Day11.monkey(2).targets[true] shouldBe 1
        Day11.monkey(2).targets[false] shouldBe 3

        Day11.monkey(3).items shouldBe listOf(74)
        Day11.monkey(3).operation(1) shouldBe 1 + 3
        Day11.monkey(3).operation(2) shouldBe 2 + 3
        Day11.monkey(3).test(17) shouldBe true
        Day11.monkey(3).test(17 * 2) shouldBe true
        Day11.monkey(3).test(14) shouldBe false
        Day11.monkey(3).targets[true] shouldBe 0
        Day11.monkey(3).targets[false] shouldBe 1
    }

    describe("playRound") {
        Day11.parseInput(example)

        withData(listOf(
            listOf(
                listOf(20, 23, 27, 26),
                listOf(2080, 25, 167, 207, 401, 1046),
                listOf(),
                listOf(),
            ),
            listOf(
                listOf(695, 10, 71, 135, 350),
                listOf(43, 49, 58, 55, 362),
                listOf(),
                listOf(),
            ),
            listOf(
                listOf(16, 18, 21, 20, 122),
                listOf(1468, 22, 150, 286, 739),
                listOf(),
                listOf(),
            ),
            listOf(
                listOf(491, 9, 52, 97, 248, 34),
                listOf(39, 45, 43, 258),
                listOf(),
                listOf(),
            ),
            listOf(
                listOf(15, 17, 16, 88, 1037),
                listOf(20, 110, 205, 524, 72),
                listOf(),
                listOf(),
            ),
        )) { (a, b, c, d) ->
            Day11.playRound()

            Day11.monkey(0).items shouldBe a.map { it }
            Day11.monkey(1).items shouldBe b.map { it }
            Day11.monkey(2).items shouldBe c.map { it }
            Day11.monkey(3).items shouldBe d.map { it }
        }
    }

    describe("monkeyBusiness") {
        Day11.parseInput(example)
        Day11.play(20)

        Day11.monkey(0).tests shouldBe 101
        Day11.monkey(1).tests shouldBe 95
        Day11.monkey(2).tests shouldBe 7
        Day11.monkey(3).tests shouldBe 105
        Day11.monkeyBusiness() shouldBe 10605
    }

})

val example = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".trimIndent()