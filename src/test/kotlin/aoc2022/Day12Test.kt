package aoc2022

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class Day12Test : DescribeSpec({

    describe("Graph") {
        Day12.parseInput(
            """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
            """.trimIndent()
        )
        withData("Sabqponm".toList().withIndex()) {
            Day12.Graph[it.index to 0]!!.char shouldBe it.value
        }
        withData("accszExk".toList().withIndex()) {
            Day12.Graph[it.index to 2]!!.char shouldBe it.value
        }
        withData("abdefghi".toList().withIndex()) {
            Day12.Graph[it.index to 4]!!.char shouldBe it.value
        }
    }

    /* for drawing maybe */
    describe("Coord#pathTo") {
        (0 to 0).pathTo(0 to 0) shouldBe Day12.Path.NA
        (0 to 0).pathTo(1 to 1) shouldBe Day12.Path.NA
        (1 to 1).pathTo(0 to 0) shouldBe Day12.Path.NA
        (0 to 2).pathTo(0 to 0) shouldBe Day12.Path.NA
        (0 to 0).pathTo(1 to 0) shouldBe Day12.Path.RIGHT
        (0 to 0).pathTo(0 to 1) shouldBe Day12.Path.DOWN
        (1 to 0).pathTo(0 to 0) shouldBe Day12.Path.LEFT
        (0 to 1).pathTo(0 to 0) shouldBe Day12.Path.UP
    }

   describe("Walk 2 x 2") {
        it ("shortest distance is 2") {
            Day12.parseInput("""
                Sa
                aE
                """.trimIndent())
            Day12.walkGraph()
            Day12.Graph.end.distance shouldBe 2
        }
    }

    describe("Walk 3x3") {
        it ("shortest distance is 4") {
            Day12.parseInput("""
                Sab
                aXc
                bcE
                """.trimIndent())
            Day12.walkGraph()
            Day12.Graph.end.distance shouldBe 4
        }
    }

    describe("Walk wierdly") {
        it ("shortest distance is 4") {
            Day12.parseInput("""
                Sabcde
                aXcXXf
                bXEkXg
                cXcjih
                dccXXX
                """.trimIndent())
            Day12.walkGraph(true)
            Day12.Graph.end.distance shouldBe 12
            Day12.walkGraph(false)
            Day12.Graph.end.distance shouldBe 12
        }
    }

    describe("Walk example") {
        Day12.parseInput(
            """
                Sabqponm
                abcryxxl
                accszExk
                acctuvwj
                abdefghi
            """.trimIndent()
        )
        Day12.walkGraph(false)
        Day12.Graph.end.distance shouldBe 31
        Day12.walkGraph(true)
        Day12.Graph.end.distance shouldBe 31
    }

    describe("draw") {
        // TODO
        """
            v..v<<<<
            >v.vv<<^
            .>vv>E^^
            ..v>>>^^
            ..>>>>>^
        """.trimIndent()
    }

})

