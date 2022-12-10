package aoc2022

object Day8 : AOC {
    data class Forest(val height: Int, val width: Int, val trees: List<Tree>) {
        init {
            trees.forEach {
                it.forest = this
            }
        }
        fun above(tree: Tree): List<Tree> = trees.filter { it.above(tree) }.sortedBy { it.y }
        fun below(tree: Tree): List<Tree> = trees.filter { it.below(tree) }.sortedByDescending { it.y }
        fun left(tree: Tree): List<Tree> = trees.filter { it.left(tree) }.sortedByDescending { it.x }
        fun right(tree: Tree): List<Tree> = trees.filter { it.right(tree) }.sortedBy { it.x }
    }

    data class Tree(val height: Int, val position: Pair<Int, Int>) {
        val x = position.first
        val y = position.second
        lateinit var forest: Forest
        fun above(other: Tree): Boolean = (x == other.x && y > other.y)
        fun above() = forest.above(this)
        fun below(other: Tree): Boolean = (x == other.x && y < other.y)
        fun below() = forest.below(this)
        fun left(other: Tree): Boolean = (x < other.x && y == other.y)
        fun left() = forest.left(this)
        fun right(other: Tree): Boolean = (x > other.x && y == other.y)
        fun right() = forest.right(this)
        fun visibleTop() = above().all { it < this }
        fun visibleLeft() = left().all { it < this }
        fun visibleRight() = right().all { it < this }
        fun visibleDown() = below().all { it < this }
        fun visible() = visibleTop() || visibleLeft() || visibleRight() || visibleDown()
        fun viewDistanceAbove() = above().let {
            minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
        }
        fun viewDistanceBelow() = below().let {
            minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
        }
        fun viewDistanceLeft() = left().let {
            minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
        }
        fun viewDistanceRight() = right().let {
            minOf(it.size, it.takeWhile { tree -> tree < this }.size + 1)
        }
        fun scenicScore() = viewDistanceAbove() * viewDistanceLeft() * viewDistanceRight() * viewDistanceBelow()
        operator fun compareTo(tree: Tree) = height.compareTo(tree.height)
    }

    override fun solve(): String {
        val lines = "day8.input".read().lines().reversed()
        val trees = lines.flatMapIndexed { y, row ->
            row.mapIndexed { x, cell ->
                Tree(cell.digitToInt(), x to y)
            }
        }
        val forest = Forest(lines.size, lines.first().length, trees)
        val visible = forest.trees.filter(Tree::visible)
        val best = forest.trees.maxBy(Tree::scenicScore)
        return """
            |## Day 8
            |* Part 1: 
            | * ${visible.size}
            |* Part 2: 
            | * ${best.scenicScore()}
        """.trimMargin()
    }
}