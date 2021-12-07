package day07

import readInput
import kotlin.math.abs
import kotlin.math.min

fun main() {

    fun parseCrabsPositions(input: List<String>) = input.first().split(",").map { it.toInt() }

    fun part1(input: List<String>): Int {
        val crabsPositions = parseCrabsPositions(input).toIntArray()

        val maxPosition = crabsPositions.maxOrNull() ?: 0

        val costs = IntArray(maxPosition + 1) { Int.MAX_VALUE }
        var numberOfLeftCrabs = 0
        var numberOfRightCrabs = crabsPositions.count { it != 0 }
        costs[0] = crabsPositions.filter { position -> position != 0 }.sum()

        var minFuelCost = costs[0]
        for (i in 1..maxPosition) {
            numberOfLeftCrabs += crabsPositions.count { it == i - 1 }
            costs[i] = costs[i - 1] - numberOfRightCrabs + numberOfLeftCrabs
            numberOfRightCrabs -= crabsPositions.count { it == i }
            minFuelCost = min(minFuelCost, costs[i])
        }

        return minFuelCost
    }

    fun part2(input: List<String>): Int {
        val crabsPositions = parseCrabsPositions(input).toIntArray()

        val maxPosition = crabsPositions.maxOrNull() ?: 0

        val costs = IntArray(maxPosition + 1) { Int.MAX_VALUE }
        var leftCrabsLastStepCost: Int
        var rightCrabsLastStepCost = crabsPositions.filter { it != 0 }.sumOf { it }
        costs[0] = crabsPositions.filter { it != 0 }.sumOf {
            IntRange(0, it).sum()
        }

        var minFuelCost = costs[0]
        for (i in 1..maxPosition) {
            leftCrabsLastStepCost = crabsPositions.filter { it < i }.sumOf { abs(it - i) }
            costs[i] = costs[i - 1] - rightCrabsLastStepCost + leftCrabsLastStepCost
            rightCrabsLastStepCost = crabsPositions.filter { it >= i }.sumOf { it - i }
            minFuelCost = min(minFuelCost, costs[i])
        }

        return minFuelCost
    }

    println("****** Advent Of Code Day 07 ******")
    println("****** Crabs Supermassive Blast Hole ******")

    val testInput = readInput("Day07_test", 7)
    val expectedPart1 = 37
    val expectedPart2 = 168
    val actualPart1 = part1(testInput)
    val actualPart2 = part2(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part2 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day07", 7)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}