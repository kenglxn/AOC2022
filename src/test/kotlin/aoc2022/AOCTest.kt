package aoc2022

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class AOCTest : DescribeSpec({

    describe("aoc2022.Day9") {
        context("Point#follow") {
            withData(listOf(
                (Day9.Knot(0, 0, 0) to Day9.Knot(0, 0, 1)) to Day9.Knot(0, 0, 1),
                (Day9.Knot(1, 0, 0) to Day9.Knot(0, 0, 1)) to Day9.Knot(0, 0, 1),
                (Day9.Knot(2, 0, 0) to Day9.Knot(0, 0, 1)) to Day9.Knot(1, 0, 1),
                (Day9.Knot(3, 0, 0) to Day9.Knot(1, 0, 1)) to Day9.Knot(2, 0, 1),
                (Day9.Knot(4, 0, 0) to Day9.Knot(2, 0, 1)) to Day9.Knot(3, 0, 1),
                (Day9.Knot(4, 1, 0) to Day9.Knot(3, 0, 1)) to Day9.Knot(3, 0, 1),
                (Day9.Knot(4, 2, 0) to Day9.Knot(3, 0, 1)) to Day9.Knot(4, 1, 1),
                (Day9.Knot(4, 3, 0) to Day9.Knot(4, 1, 1)) to Day9.Knot(4, 2, 1),
                (Day9.Knot(4, 4, 0) to Day9.Knot(4, 2, 1)) to Day9.Knot(4, 3, 1),
                (Day9.Knot(3, 4, 0) to Day9.Knot(4, 3, 1)) to Day9.Knot(4, 3, 1),
                (Day9.Knot(2, 4, 0) to Day9.Knot(4, 3, 1)) to Day9.Knot(3, 4, 1),
                (Day9.Knot(2, 2, 4) to Day9.Knot(0, 0, 5)) to Day9.Knot(1, 1, 5),
            )) { (state, result) ->
                val (head, tail) = state
                tail.follow(head) shouldBe result
            }
        }

        context("Rope#move") {
            it("moves correctly") {
                val rope = Day9.Rope(
                    mutableListOf(
                        Day9.Knot(4, 3, 0),
                        Day9.Knot(4, 2, 1),
                        Day9.Knot(3, 1, 2),
                        Day9.Knot(2, 1, 3),
                        Day9.Knot(1, 1, 4),
                        Day9.Knot(0, 0, 5),
                        Day9.Knot(0, 0, 6),
                        Day9.Knot(0, 0, 7),
                        Day9.Knot(0, 0, 8),
                    )
                )
                rope.move(Day9.Direction.U, 1)
                rope.print()
                rope.states.last().first shouldBe listOf(
                    Day9.Knot(4, 4, 0),
                    Day9.Knot(4, 3, 1),
                    Day9.Knot(4, 2, 2),
                    Day9.Knot(3, 2, 3),
                    Day9.Knot(2, 2, 4),
                    Day9.Knot(1, 1, 5),
                    Day9.Knot(0, 0, 6),
                    Day9.Knot(0, 0, 7),
                    Day9.Knot(0, 0, 8),
                )
            }
        }
    }
})
