package day04

import readInput
import java.lang.IllegalArgumentException

data class Board(val lines: List<Line>)

data class Line(val numbers: MutableList<Int>)

//data class BoardNumber(val number: Int, val isMarked: Boolean)


fun checkRows(board: Board): Boolean {
    board.lines.forEach { line ->
        val bingo = line.numbers.all { it == -1 }
        if (bingo) return true
    }
    return false
}

fun checkColumns(board: Board): Boolean {
    board.lines.indices.forEach { columnIndex ->
        val bingo = board.lines.map { it.numbers[columnIndex] }.all { it == -1 }
        if (bingo) return true
    }
    return false
}

fun sumOfUnmarked(board: Board): Int {
    return board.lines.map { line: Line -> line.sumOfUnmarked() }.sum()
}

fun mark(drawNumber: Int, board: Board) {
    board.lines.forEach { line ->
        line.numbers.replaceAll { number ->
            if (number == drawNumber) {
                -1
            } else {
                number
            }
        }
    }
}

fun Line.sumOfUnmarked() = numbers.filter { it != -1 }.sum()

fun readBoard(input: List<String>, startLine: Int, boardSize: Int): Board {
    //read boardSize x BoardSize board
    if (startLine + boardSize > input.size) throw IllegalArgumentException("Input data has not completed boards")

    val boardLines = mutableListOf<Line>()

    (startLine until startLine + boardSize).forEach { line ->
        boardLines.add(Line(input[line].toNumbers(' ')))
    }

    return Board(boardLines)
}

fun main() {
    val boardSize = 5

    fun readBoards(
        input: List<String>,
        boardSize: Int
    ): List<Board> {
        var currentLine = 1
        val boards = mutableListOf<Board>()
        while (currentLine < input.size) {
            if (input[currentLine].isEmpty() || input[currentLine].isBlank()) {
                currentLine++
                continue
            }
            boards.add(readBoard(input, currentLine, boardSize))
            currentLine += boardSize
        }
        return boards
    }

    fun isWinner(board: Board) = checkRows(board) || checkColumns(board)

    fun part1(input: List<String>): Int {
        val drawnNumbers = input.first().toNumbers(',')
        //read boards
        val boards = readBoards(input, boardSize)

        //condition of winner : row or column is marked. (Naive approach is add for each element in board flag isMarked and check
        // after each draw if row or column completely marked?)
        //we can mark numbers replacing them with -1
        //we need three methods: checkLines, checkColumns, calcSumOfUnmarked in board
        var currentDrawIndex = 0
        var bingoBoardIndex = -1
        while (currentDrawIndex < drawnNumbers.size && bingoBoardIndex == -1) {
            val drawNumber = drawnNumbers[currentDrawIndex]
            boards.forEachIndexed { index, board ->
                mark(drawNumber, board)

                //What we can do to reuse code ?
                if (isWinner(board)) {
                    bingoBoardIndex = index
                }
            }

            currentDrawIndex++
        }

        currentDrawIndex--

        return if (bingoBoardIndex != -1) {
            drawnNumbers[currentDrawIndex] * sumOfUnmarked(boards[bingoBoardIndex])
        } else {
            -1
        }
    }

    fun part2(input: List<String>): Int {
        var currentDrawIndex = 0
        var lastBingoBoardIndex = -1
        val winnerBoardsIndices: MutableList<Int> = mutableListOf()

        val drawnNumbers = input.first().toNumbers(',')
        val boards = readBoards(input, boardSize)

        while (currentDrawIndex < drawnNumbers.size && winnerBoardsIndices.size < boards.size) {
            val drawNumber = drawnNumbers[currentDrawIndex]
            boards.forEachIndexed { index, board ->
                if (!winnerBoardsIndices.contains(index)) {
                    mark(drawNumber, board)
                    if (isWinner(board)) {
                        winnerBoardsIndices.add(index)
                        lastBingoBoardIndex = index
                    }
                }
            }
            currentDrawIndex++
        }

        currentDrawIndex--

        val lastNumber = drawnNumbers[currentDrawIndex]
        val sumOfUnmarked = sumOfUnmarked(boards[lastBingoBoardIndex])

        return if (lastBingoBoardIndex != -1) {
            lastNumber * sumOfUnmarked
        } else {
            -1
        }
    }

    println("**** Advent Of Code Day 04 ****")
    val testInput = readInput("Day04_test", 4)
    val expectedPart1 = 4512
    val expectedPart2 = 1924
    val actualPart1 = part1(testInput)
    val actualPart2 = part2(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part2 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day04", 4)
    println("real data: part1 = ${part1(input)}")
    println("real data: part2 = ${part2(input)}")
}

fun String.toNumbers(delimiter: Char): MutableList<Int> =
    split(delimiter).filter { it.isNotBlank() && it.isNotEmpty() }.map { it.toInt() }.toMutableList()