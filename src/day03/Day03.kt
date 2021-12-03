package day03

import readInput

fun main() {

    fun powerConsumption(input: List<String>): Int {
        //read all Strings from input
        //create columns from input
        //calculate most common bit, least common bit will be a reverse
        //convert to decimal
        //multiple most common and least common bits values -> result

        val mostCommonBits = input.mostCommonBitsInColumns()
        val leastCommonBits = mostCommonBits.invertBits()

        val gammaRate = mostCommonBits.toDecimal()
        val epsilonRate = leastCommonBits.toDecimal()

        return gammaRate * epsilonRate
    }

    fun lifeSupportingRate(input: List<String>): Int {

        val digitsList = input.toListOfDigits()

        var matchingNumbersOxygenGenerator = digitsList
        var matchingNumbersCO2ScrubberCriteria = digitsList

        val numberOfBits = digitsList.first().size
        var currentBitIndex = 0

        println("***** Oxygen Generator *****")
        while (matchingNumbersOxygenGenerator.size > 1 && currentBitIndex < numberOfBits) {
            //i-th column of digits
            val ithDigits = matchingNumbersOxygenGenerator.getIthDigits(currentBitIndex)

            //get the most common digit
            val mostCommonDigit = if (ithDigits.count { it == 1 } >= ithDigits.count { it == 0 }) 1 else 0
            //filter matchingNumbers by most common digit
            matchingNumbersOxygenGenerator =
                matchingNumbersOxygenGenerator.filter { digits -> digits[currentBitIndex] == mostCommonDigit }
            println("i = $currentBitIndex, mcd = $mostCommonDigit,  matching = $matchingNumbersOxygenGenerator")
            currentBitIndex++
        }

        currentBitIndex = 0

        println("***** CO2 Scrubber *****")
        while (matchingNumbersCO2ScrubberCriteria.size > 1 && currentBitIndex < numberOfBits) {
            //i-th column of digits
            val ithDigits = matchingNumbersCO2ScrubberCriteria.getIthDigits(currentBitIndex)

            //get the most common digit
            val leastCommonDigit = if (ithDigits.count { it == 1 } < ithDigits.count { it == 0 }) 1 else 0
            //filter matchingNumbers by most common digit
            matchingNumbersCO2ScrubberCriteria =
                matchingNumbersCO2ScrubberCriteria.filter { digits -> digits[currentBitIndex] == leastCommonDigit }

            println("i = $currentBitIndex, lcd = $leastCommonDigit, matching = $matchingNumbersCO2ScrubberCriteria")
            currentBitIndex++
        }

        val oxygenGenerator = matchingNumbersOxygenGenerator.first().toDecimal()
        val co2Scrubber = matchingNumbersCO2ScrubberCriteria.first().toDecimal()
        println("oxygenGenerator = $oxygenGenerator, co2Scrubber = $co2Scrubber")

        //this is life support rating!!!
        return oxygenGenerator * co2Scrubber
    }

    println("**** Advent Of Code Day 03 ****")
    val testInput = readInput("Day03_test", 3)
    val expectedPart1 = 198
    val expectedPart2 = 230
    val actualPart1 = powerConsumption(testInput)
    val actualPart2 = lifeSupportingRate(testInput)
    println("test data, part1 = $actualPart1")
    println("test data, part2 = $actualPart2")
    check(actualPart1 == expectedPart1)
    check(actualPart2 == expectedPart2)

    val input = readInput("Day03", 3)
    println("real data: part1 = ${powerConsumption(input)}")
    println("real data: part2 = ${lifeSupportingRate(input)}")
}

fun List<String>.toListOfDigits(): List<List<Int>> = this.map { it.toDigits() }

fun List<List<Int>>.getIthDigits(i: Int): List<Int> = this.map { it[i] }

fun String.toDigits(): List<Int>  {
    return this.toCharArray().map { it.digitToInt() }
}

fun List<String>.mostCommonBitsInColumns(): List<Int> {
    val positionMostCommonBits: MutableList<Int> = MutableList(first().length) { 0 }

    toListOfDigits().forEach { digits ->
        digits.mapIndexed { position, digit ->
            if (digit == 0) {
                positionMostCommonBits[position] -= 1
            } else {
                positionMostCommonBits[position] += 1
            }
        }
    }

    return positionMostCommonBits.map { if (it < 0) 0 else 1 }
}

fun List<Int>.invertBits() = map { if (it == 0) 1 else 0 }

fun List<Int>.toDecimal() = joinToString("").toInt(2)