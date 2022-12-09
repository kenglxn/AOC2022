import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class AOCTest : DescribeSpec({

    describe("Day9") {
        context("Point#follow") {
            withData(listOf(
                (AOC.Day9.Knot(0, 0, 0) to AOC.Day9.Knot(0, 0, 1)) to AOC.Day9.Knot(0, 0, 1),
                (AOC.Day9.Knot(1, 0, 0) to AOC.Day9.Knot(0, 0, 1)) to AOC.Day9.Knot(0, 0, 1),
                (AOC.Day9.Knot(2, 0, 0) to AOC.Day9.Knot(0, 0, 1)) to AOC.Day9.Knot(1, 0, 1),
                (AOC.Day9.Knot(3, 0, 0) to AOC.Day9.Knot(1, 0, 1)) to AOC.Day9.Knot(2, 0, 1),
                (AOC.Day9.Knot(4, 0, 0) to AOC.Day9.Knot(2, 0, 1)) to AOC.Day9.Knot(3, 0, 1),
                (AOC.Day9.Knot(4, 1, 0) to AOC.Day9.Knot(3, 0, 1)) to AOC.Day9.Knot(3, 0, 1),
                (AOC.Day9.Knot(4, 2, 0) to AOC.Day9.Knot(3, 0, 1)) to AOC.Day9.Knot(4, 1, 1),
                (AOC.Day9.Knot(4, 3, 0) to AOC.Day9.Knot(4, 1, 1)) to AOC.Day9.Knot(4, 2, 1),
                (AOC.Day9.Knot(4, 4, 0) to AOC.Day9.Knot(4, 2, 1)) to AOC.Day9.Knot(4, 3, 1),
                (AOC.Day9.Knot(3, 4, 0) to AOC.Day9.Knot(4, 3, 1)) to AOC.Day9.Knot(4, 3, 1),
                (AOC.Day9.Knot(2, 4, 0) to AOC.Day9.Knot(4, 3, 1)) to AOC.Day9.Knot(3, 4, 1),
                (AOC.Day9.Knot(2, 2, 4) to AOC.Day9.Knot(0, 0, 5)) to AOC.Day9.Knot(1, 1, 5),
            )) { (state, result) ->
                val (head, tail) = state
                tail.follow(head) shouldBe result
            }
        }

        context("Rope#move") {
            it("moves correctly") {
                val rope = AOC.Day9.Rope(
                    mutableListOf(
                        AOC.Day9.Knot(4, 3, 0),
                        AOC.Day9.Knot(4, 2, 1),
                        AOC.Day9.Knot(3, 1, 2),
                        AOC.Day9.Knot(2, 1, 3),
                        AOC.Day9.Knot(1, 1, 4),
                        AOC.Day9.Knot(0, 0, 5),
                        AOC.Day9.Knot(0, 0, 6),
                        AOC.Day9.Knot(0, 0, 7),
                        AOC.Day9.Knot(0, 0, 8),
                    )
                )
                rope.move(AOC.Day9.Direction.U, 1)
                rope.print()
                rope.states.last().first shouldBe listOf(
                    AOC.Day9.Knot(4, 4, 0),
                    AOC.Day9.Knot(4, 3, 1),
                    AOC.Day9.Knot(4, 2, 2),
                    AOC.Day9.Knot(3, 2, 3),
                    AOC.Day9.Knot(2, 2, 4),
                    AOC.Day9.Knot(1, 1, 5),
                    AOC.Day9.Knot(0, 0, 6),
                    AOC.Day9.Knot(0, 0, 7),
                    AOC.Day9.Knot(0, 0, 8),
                )
            }
        }
    }
})
