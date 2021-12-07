package day06

import readInput

fun main() {

    fun simulateFishGrowth(numberOfDays: Int, initialState: Collection<Int>): Long {
        val fishCount = LongArray(9) { 0 }

        initialState.forEach { fishCount[it]++ }

        var currentDay = 1

        while (currentDay <= numberOfDays) {

            val tempZeroCounts = fishCount[0]
            fishCount[0] = 0
            for (i in 0..7) {
                fishCount[i] += fishCount[i+1]
                fishCount[i+1] = 0
            }
            fishCount[8] += tempZeroCounts
            fishCount[6] += tempZeroCounts
            currentDay++
        }

        return fishCount.sum()

    }

    fun parseInitialState(input: List<String>) = input.first().split(",").map { it.toInt() }

    fun part1(input: List<String>): Int {
        return simulateFishGrowth(
            numberOfDays = 80,
            initialState = parseInitialState(input)
        ).toInt()
    }

    fun part2(input: List<String>): Long {
        return simulateFishGrowth(
            numberOfDays = 256,
            initialState = parseInitialState(input)
        )
    }

    println("****** Advent Of Code Day 06 ******")
    println("****** Massive Glowing Lanternfish ******")

    val testInput = readInput("Day06_test", 6)
    val expectedPart1 = 5934
    val expectedPart2 = 26984457539
    val actualPart1 = part1(testInput)
    val actualPart2 = part2(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part2 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day06", 6)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}