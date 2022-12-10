package aoc2022

object Day6 : AOC {
    fun String.markerWithSize(markerSize: Int) =
        this.withIndex()
            .windowed(markerSize)
            .find {
                it.map { v -> v.value }.toSet().size == markerSize
            }!!.last().index + 1

    fun String.startOfPacket() = markerWithSize(4)
    fun String.startOfMessage() = markerWithSize(14)

    override fun solve(): String {
        val input = "day6.input".read()
        val packetMarker = input.startOfPacket()
        val messageMarker = input.startOfMessage()

        return """
            |## Day 6
            |* Part 1: 
            | * $packetMarker
            |* Part 2: 
            | * $messageMarker
        """.trimMargin()
    }
}