package day01

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val depths = input.map { it.toInt() }

        var numberOfIncreases = 0
        for (i in 1 until depths.size) {
            if (depths[i - 1] < depths[i]) {
                numberOfIncreases++
            }
        }
        return numberOfIncreases
    }

    fun part2(input: List<String>): Int {
        val slidingWindowSize = 3
        //not enough data to create sliding window of 3
        if (input.size < slidingWindowSize) return 0

        var numberOfIncreases = 0
        val depth = input.map { it.toInt() }
//        depth.windowed().zipWithNext().count { (first: Int, second: Int) -> second > first }
        var windowStart = 0
        var windowEnd = slidingWindowSize - 1
        var previousSlidingWindowSum = depth.subList(windowStart, windowEnd).sum()

        while (windowEnd < depth.size - 1) {
            windowStart++
            windowEnd++
            val currentSum = previousSlidingWindowSum - depth[windowStart - 1] + depth[windowEnd]
            if (currentSum > previousSlidingWindowSum) numberOfIncreases++
            previousSlidingWindowSum = currentSum
        }
        return numberOfIncreases
    }

    val testInput = readInput("Day01_test", 1)
    val expectedPart1 = 7
    val actualPart1 = part1(testInput)
    println("test data, part1 = $actualPart1")
    check(part1(testInput) == expectedPart1)

    val expectedPart2 = 5
    val actualPart2 = part2(testInput)
    println("test data, part2 = $actualPart2")
    check(actualPart2 == expectedPart2)

    val input = readInput("Day01", 1)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}