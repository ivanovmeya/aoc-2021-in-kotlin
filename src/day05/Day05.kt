package day05

import readInput
import kotlin.math.max

fun main() {
    data class VentPoint(val x: Int, val y: Int)
    data class VentLine(val start: VentPoint, val end: VentPoint) {
        fun isVertical(): Boolean = start.x == end.x
        fun isHorizontal(): Boolean = start.y == end.y
        fun isDiagonal(): Boolean = !isVertical() && !isHorizontal()
        fun toPoints(): List<VentPoint> {
            val points = mutableListOf<VentPoint>()
            return when {
                isVertical() -> {
                    val x = start.x
                    val startY = start.y
                    val step = if (start.y > end.y) -1 else 1

                    val yRange = IntProgression.fromClosedRange(startY, end.y, step)
                    for (y in yRange) {
                        points.add(VentPoint(x, y))
                    }
                    points
                }
                isHorizontal() -> {
                    val startX = start.x
                    val y = start.y
                    val step = if (start.x > end.x) -1 else 1

                    val xRange = IntProgression.fromClosedRange(startX, end.x, step)
                    for (x in xRange) {
                        points.add(VentPoint(x, y))
                    }
                    points
                }
                else -> {
                    val xStep = if (start.x > end.x) -1 else 1
                    val yStep = if (start.y > end.y) -1 else 1

                    val xRange = IntProgression.fromClosedRange(start.x, end.x, xStep)
                    val yRange = IntProgression.fromClosedRange(start.y, end.y, yStep)

                    val xIterator = xRange.iterator()
                    val yIterator = yRange.iterator()
                    while (xIterator.hasNext() && yIterator.hasNext()) {
                        points.add(
                            VentPoint(
                                xIterator.nextInt(),
                                yIterator.nextInt()
                            )
                        )
                    }
                    points
                }
            }
        }

        override fun toString(): String {
            return "[${start.x},${start.y}] -> [${end.x},${end.y}]"
        }
    }

    fun String.parseVentsEdgePoints() = split("->").map {
        val (x, y) = it.trim().split(",")
        VentPoint(x.toInt(), y.toInt())
    }

    fun List<VentPoint>.toVentLine() = VentLine(
        VentPoint(first().x, first().y),
        VentPoint(last().x, last().y)
    )

    fun calcTotalDangerousPoints(dangerMap: MutableList<MutableList<Int>>) =
        dangerMap
            .map { line -> line.count { it >= 2 } }
            .sum()

    fun detectDangerousVentPointsCount(input: List<String>, withDiagonalLines: Boolean): Int {
        var maxX = 0
        var maxY = 0

        val ventLines = input.map {
            val points = it.parseVentsEdgePoints()
            maxX = max(maxX, points.maxOf { point -> point.x })
            maxY = max(maxY, points.maxOf { point -> point.y })
            points.toVentLine()
        }.let { ventLines ->
            if (!withDiagonalLines) {
                ventLines.filter { !it.isDiagonal() }
            } else {
                ventLines
            }
        }

        //map lines to danger map with points overlaps
        val size = max(maxX, maxY) + 1
        //could use hashmap for efficiency (save only dangerous points)
        val dangerMap = MutableList(size) { MutableList(size) { 0 } }

        //fill dangerMap
        ventLines.forEach { ventLine ->
            val linePoints = ventLine.toPoints()
            linePoints.forEach {
                dangerMap[it.x][it.y] += 1
            }
        }

        return calcTotalDangerousPoints(dangerMap)
    }

    fun part1(input: List<String>): Int {
        return detectDangerousVentPointsCount(input, false)
    }

    fun part2(input: List<String>): Int {
        return detectDangerousVentPointsCount(input, true)
    }

    println("****** Advent Of Code Day 05 ******")
    println("****** Hydrothermal Vents detected! ******")

    val testInput = readInput("Day05_test", 5)
    val expectedPart1 = 5
    val expectedPart2 = 12
    val actualPart1 = part1(testInput)
    val actualPart2 = part2(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part2 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day05", 5)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}