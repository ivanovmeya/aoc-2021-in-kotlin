package day11

import readInput

fun <T, R> Map<T, R>.keyForValue(value: R) = this.filterValues { it == value }.keys.first()

fun main() {

    fun String.toDigits(): List<Int> = this.toCharArray().map { it.digitToInt() }

    fun List<String>.parseOctopusMap() = MutableList(size) { index ->
        this[index].toDigits().toMutableList()
    }

    fun List<List<Int>>.printOctopusMap() {
        println("Octopus Map:")
        this.forEach {
            println(it.joinToString(""))
        }
    }

    data class OctopusCoordinates(val row: Int, val column: Int)

    fun MutableList<MutableList<Int>>.incEnergyLevels() {
        for (row in this.indices) {
            for (column in this.first().indices) {
                this[row][column] += 1
            }
        }
    }

    fun MutableList<MutableList<Int>>.cleanEnergyLevel(flashedOctopus: List<OctopusCoordinates>) {
        flashedOctopus.forEach { coordinate ->
            this[coordinate.row][coordinate.column] = 0
        }
    }

    fun MutableList<MutableList<Int>>.adjacentFor(row: Int, column: Int): List<OctopusCoordinates> {
        val adjacentList = mutableListOf<OctopusCoordinates>()

        val minRow = (row - 1).coerceAtLeast(0)
        val maxRow = (row + 1).coerceAtMost(this.size - 1)
        val minColumn = (column - 1).coerceAtLeast(0)
        val maxColumn = (column + 1).coerceAtMost(this.first().size - 1)

        for (r in minRow..maxRow) {
            for (c in minColumn..maxColumn) {
                adjacentList.add(OctopusCoordinates(r, c))
            }
        }

        adjacentList.removeIf { it.row == row && it.column == column }
        return adjacentList
    }

    fun flash(
        coordinates: OctopusCoordinates,
        octopusMap: MutableList<MutableList<Int>>,
        flashedOctopus: MutableList<OctopusCoordinates>
    ) {
        flashedOctopus.add(coordinates)
        val adjacents = octopusMap.adjacentFor(coordinates.row, coordinates.column)
        adjacents.forEach { adjacentCoordinates ->
            octopusMap[adjacentCoordinates.row][adjacentCoordinates.column] += 1
            if (!flashedOctopus.contains(adjacentCoordinates) && octopusMap[adjacentCoordinates.row][adjacentCoordinates.column] > 9) {
                flash(adjacentCoordinates, octopusMap, flashedOctopus)
            }
        }
    }

    fun MutableList<MutableList<Int>>.performChainingFlash(flashedOctopus: MutableList<OctopusCoordinates>) {
        for (row in this.indices) {
            for (column in this.first().indices) {
                val coordinates = OctopusCoordinates(row, column)
                if (this[row][column] > 9 && !flashedOctopus.contains(coordinates)) {
                    flash(coordinates, this, flashedOctopus)
                }
            }
        }

    }

    fun part1(input: List<String>): Int {
        val octopusMap = input.parseOctopusMap()
        val numberOfSteps = 100
        var totalFlashes = 0
        for (step in 1..numberOfSteps) {
            val flashedOctopus = mutableListOf<OctopusCoordinates>()
            octopusMap.incEnergyLevels()
            octopusMap.performChainingFlash(flashedOctopus)
            octopusMap.cleanEnergyLevel(flashedOctopus)
            totalFlashes += flashedOctopus.size
        }

        return totalFlashes
    }

    fun part2(input: List<String>): Int {
        val octopusMap = input.parseOctopusMap()
        var currentStep = 1
        val flashedOctopus = mutableListOf<OctopusCoordinates>()

        //all octopus shine bright like a diamond at once
        while (flashedOctopus.size != octopusMap.size * octopusMap.first().size) {
            flashedOctopus.clear()
            octopusMap.incEnergyLevels()
            octopusMap.performChainingFlash(flashedOctopus)
            octopusMap.cleanEnergyLevel(flashedOctopus)
            currentStep++
        }

        return currentStep - 1
    }

    println("****** Advent Of Code Day 11 ******")
    println("********** Dumbo Octopus **********")

    val day = 11
    val dayString = if (day < 10) "0${day}" else "$day"
    val testInput = readInput("Day${dayString}_test", day)
    val expectedPart1 = 1656
    val expectedPart2 = 195
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