package day10

import readInput
import java.util.*

fun <T, R> Map<T, R>.keyForValue(value: R) = this.filterValues { it == value }.keys.first()

fun main() {

    fun firstScoreFor(char: Char): Int = when (char) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw IllegalArgumentException("unknown character = $char")
    }

    fun secondScoreFor(char: Char): Int = when (char) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw IllegalArgumentException("unknown character = $char")
    }

    val matchingCharacters = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    fun openCharacterFor(close: Char) = matchingCharacters[close]!!
    fun closeCharacterFor(open: Char) = matchingCharacters.keyForValue(open)
    fun openCharacters() = matchingCharacters.values
    fun closeCharacters() = matchingCharacters.keys

    fun findInvalidCharacter(line: String): Char? {
        var invalidChar: Char? = null
        val stack = ArrayDeque<Char>()
        for (char in line.toCharArray()) {
            if (char in openCharacters()) {
                stack.push(char)
            } else {
                val topChar = stack.poll()
                if (topChar != openCharacterFor(char)) {
                    invalidChar = char
                    break
                }
                while (stack.peek() in closeCharacters()) {
                    stack.poll()
                    stack.poll()
                }
            }
        }
        return invalidChar
    }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            val invalidChar = findInvalidCharacter(line)
            invalidChar?.let { firstScoreFor(invalidChar) } ?: 0
        }.sumOf { it }
    }

    fun part2(input: List<String>): Long {
        val incompleteStrings = input.filter { findInvalidCharacter(it) == null }
        val scores = incompleteStrings.map { line ->
            val missingCharacters = mutableListOf<Char>()
            val stack = ArrayDeque<Char>()
            for (char in line.toCharArray().reversed()) {
                if (char in closeCharacters()) {
                    stack.push(char)
                } else {
                    if (stack.isEmpty()) {
                        missingCharacters.add(closeCharacterFor(char))
                    } else {
                        stack.poll()
                    }
                }
            }
            missingCharacters.fold(0L) { acc, c ->
                (acc * 5 + secondScoreFor(c))
            }
        }.sorted()

        return scores[scores.size / 2]
    }

    println("****** Advent Of Code Day 10 ******")
    println("*********** Syntax Scoring ***********")

    val day = 10
    val dayString = if (day < 10) "0${day}" else "$day"
    val testInput = readInput("Day${dayString}_test", day)
    val expectedPart1 = 26397
    val expectedPart2 = 288957L
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