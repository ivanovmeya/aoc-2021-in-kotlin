package day02

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        var depth = 0
        var distance = 0
        input.forEach { inputString ->
            val (command, valueString) = inputString.split(' ')
            val value = valueString.toInt()
            when (command) {
                "forward" -> distance += value
                "up" -> depth -= value
                "down" -> depth += value
            }
        }

        return depth * distance
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var distance = 0
        var aim = 0
        input.forEach { inputString ->
            val (command, valueString) = inputString.split(' ')
            val value = valueString.toInt()
            when (command) {
                "forward" -> {
                    distance += value
                    depth += aim * value
                }
                "up" -> aim -= value
                "down" -> aim += value
            }
        }

        return depth * distance
    }

    println("**** Advent Of Code Day 02 ****")
    val testInput = readInput("Day02_test",2)
    val expectedPart1 = 150
    val expectedPart2 = 900
    val actualPart1 = part1(testInput)
    val actualPart2 = part2(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part1 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day02",2)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}