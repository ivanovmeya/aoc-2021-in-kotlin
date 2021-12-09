package day09

import readInput
import java.util.*

data class Point(val row: Int, val column: Int)

fun getPointAdjacents(heightMap: List<List<Int>>, point: Point, predicate: (Point) -> Boolean): List<Point> {
    val adjacents = mutableListOf<Point>()

    //top
    if (point.row - 1 >= 0) {
        Point(point.row - 1, point.column).let {
            if (predicate(it)) {
                adjacents.add(it)
            }
        }
    }

    //bottom
    if (point.row + 1 < heightMap.size) {
        Point(point.row + 1, point.column).let {
            if (predicate(it)) {
                adjacents.add(it)
            }
        }
    }

    //left
    if (point.column - 1 >= 0) {
        Point(point.row, point.column - 1).let {
            if (predicate(it)) {
                adjacents.add(it)
            }
        }
    }

    //right
    if (point.column + 1 < heightMap.first().size) {
        Point(point.row, point.column + 1).let {
            if (predicate(it)) {
                adjacents.add(it)
            }
        }
    }

    return adjacents
}

fun checkLeft(heightMap: List<List<Int>>, rowIndex: Int, columnIndex: Int): Boolean {
    return if (columnIndex - 1 >= 0) {
        heightMap[rowIndex][columnIndex - 1] > heightMap[rowIndex][columnIndex]
    } else {
        true
    }
}

fun checkRight(heightMap: List<List<Int>>, rowIndex: Int, columnIndex: Int): Boolean {
    return if (columnIndex + 1 < heightMap.first().size) {
        heightMap[rowIndex][columnIndex + 1] > heightMap[rowIndex][columnIndex]
    } else {
        true
    }
}

fun checkTop(heightMap: List<List<Int>>, rowIndex: Int, columnIndex: Int): Boolean {
    return if (rowIndex - 1 >= 0) {
        heightMap[rowIndex - 1][columnIndex] > heightMap[rowIndex][columnIndex]
    } else {
        true
    }
}

fun checkBottom(heightMap: List<List<Int>>, rowIndex: Int, columnIndex: Int): Boolean {
    return if (rowIndex + 1 < heightMap.size) {
        heightMap[rowIndex + 1][columnIndex] > heightMap[rowIndex][columnIndex]
    } else {
        true
    }
}

fun main() {

    fun List<String>.parseInput(): List<List<Int>> {
        return List(this.size) { rowIndex ->
            this[rowIndex].toCharArray().map { it.digitToInt() }
        }
    }

    fun calcLowPoints(heightMap: List<List<Int>>): MutableList<Point> {
        val lowPoints = mutableListOf<Point>()

        for (rowIndex in heightMap.indices) {
            for (columnIndex in heightMap.first().indices) {
                val isLowPoint = checkLeft(heightMap, rowIndex, columnIndex) &&
                        checkTop(heightMap, rowIndex, columnIndex) &&
                        checkRight(heightMap, rowIndex, columnIndex) &&
                        checkBottom(heightMap, rowIndex, columnIndex)
                if (isLowPoint) {
                    lowPoints.add(Point(rowIndex, columnIndex))
                }
            }
        }
        return lowPoints
    }

    fun part1(input: List<String>): Int {
        val heightMap = input.parseInput()
        val lowPoints = calcLowPoints(heightMap)

        return lowPoints.sumOf { heightMap[it.row][it.column] + 1 }
    }


    fun part2(input: List<String>): Int {
        val heightMap = input.parseInput()
        val lowPoints = calcLowPoints(heightMap)

        val basins: MutableMap<Point, MutableList<Point>> = mutableMapOf()

        val pointsToVisit: Queue<Point> = ArrayDeque<Point>()

        lowPoints.forEach { lowPoint ->
            //BFS for every low point
            val basinPoints = mutableListOf<Point>()
            pointsToVisit.add(lowPoint)

            while (pointsToVisit.isNotEmpty()) {
                val currentPoint = pointsToVisit.poll()
                if (!basinPoints.contains(currentPoint))
                    basinPoints.add(currentPoint)
                val adjacents = getPointAdjacents(heightMap, currentPoint) {
                    val adjacentValue = heightMap[it.row][it.column]
                    val currentValue = heightMap[currentPoint.row][currentPoint.column]
                    adjacentValue != 9 && !basinPoints.contains(it) && adjacentValue > currentValue
                }
                pointsToVisit.addAll(adjacents)
            }
            basins[lowPoint] = basinPoints
        }

        return basins.values.sortedByDescending { it.size }.take(3).fold(1) { acc, mutableList ->
            acc * mutableList.size
        }
    }


    println("****** Advent Of Code Day 09 ******")
    println("*********** Smoke Basin ***********")

    val day = 9
    val dayString = if (day < 10) "0${day}" else "$day"
    val testInput = readInput("Day${dayString}_test", day)
    val expectedPart1 = 15
    val expectedPart2 = 1134
    val actualPart1 = part1(testInput)
    val actualPart2 = part2(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part2 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day$dayString", day)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}