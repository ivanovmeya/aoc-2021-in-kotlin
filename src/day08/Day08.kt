package day08

import readInput


data class Entry(val patterns: List<String>, val values: List<String>)
data class DigitPattern(val value: Int, val pattern: String)
data class DigitScheme(val placements: CharArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DigitScheme

        if (!placements.contentEquals(other.placements)) return false

        return true
    }

    override fun hashCode(): Int {
        return placements.contentHashCode()
    }
}

fun List<DigitScheme>.deepCopy(): MutableList<DigitScheme> {
    val result = mutableListOf<DigitScheme>()
    forEach { result.add(it) }
    return result
}

fun main() {


    fun List<String>.parseInput() = map { entryString ->
        val (patternString, outputValueString) = entryString.split("|")
        Entry(
            patterns = patternString.trim().split(" "),
            values = outputValueString.trim().split(" ")
        )
    }


//    1: - 2 segments, and 4 digits = cc ff
//    4: - 4 segments = bb cc dddd ff
//    7: - 3 segments = aaaa cc ff
//    8: - 7 segments = aaaa bb cc dddd ee ff gggg
    fun String.isEasyDigit() = length == 2 || length == 4 || length == 3 || length == 7

    fun String.toEasyDigit() = when (length) {
        2 -> 1
        3 -> 7
        4 -> 4
        7 -> 8
        else -> throw IllegalArgumentException(" This is not easy digit, string = $this length = $length")
    }

    val validDigitsPlaces = mapOf(
        0 to listOf(0, 1, 2, 3, 4, 5),
        1 to listOf(1, 2),
        2 to listOf(0, 1, 3, 4, 6),
        3 to listOf(0, 1, 2, 3, 6),
        4 to listOf(1, 2, 5, 6),
        5 to listOf(0, 2, 3, 5, 6),
        6 to listOf(0, 2, 3, 4, 5, 6),
        7 to listOf(0, 1, 2),
        8 to listOf(0, 1, 2, 3, 4, 5, 6),
        9 to listOf(0, 1, 2, 3, 5, 6)
    )

    fun part1(input: List<String>): Int {
        val entries = input.parseInput()
        return entries.sumOf { it.values.count { valueString -> valueString.isEasyDigit() } }
    }

    fun generateAllPossibleSchemes(easyDigitPatterns: List<DigitPattern>): List<DigitScheme> {
        val possibleSchemes: MutableList<DigitScheme> = mutableListOf()

        easyDigitPatterns.forEach { digitPattern ->
            //on the first step we need to create Schemes, on others steps we need to extend existing schemes
            if (digitPattern.value == 1) {
                val places = validDigitsPlaces[digitPattern.value]!!
                //generate all permutations for places.
                val chars = digitPattern.pattern.toCharArray()

                //should be only free chars size
                val placements = CharArray(7)
                var i = 0
                places.forEach { place ->
                    placements[place] = chars[i++]
                }
                possibleSchemes.add(DigitScheme(placements))

                val reversedPlacement = CharArray(7)
                i = 0
                places.reversed().forEach { place ->
                    reversedPlacement[place] = chars[i++]
                }
                possibleSchemes.add(DigitScheme(reversedPlacement))
//                printScheme(possibleSchemes)
            } else {
                val localPossibleSchemes = possibleSchemes.deepCopy()
                localPossibleSchemes.forEach { previousScheme ->

                    //filter already busy places
//                    val freePlaces = previousScheme.placements.mapIndexed { index, c -> index to c }.filter { it.second == Char.MIN_VALUE }.map { it.first }


                    val placesToFill =
                        validDigitsPlaces[digitPattern.value]!!.filter { previousScheme.placements[it] == Char.MIN_VALUE } //check if CharArray fills with Char.MIN_VALUE by default (yes, this is 'null char')
                    //filter already placed chars
                    val chars = digitPattern.pattern.toCharArray().filter { !previousScheme.placements.contains(it) }

                    var i = 0
                    //generate new possible schemes from previous scheme  with current chars.
                    val previousSchemeCopyUpdated = previousScheme.copy(placements = previousScheme.placements.clone())
                    placesToFill.forEach { place ->
                        previousSchemeCopyUpdated.placements[place] = chars[i++]
                    }
                    if (!possibleSchemes.contains(DigitScheme(previousSchemeCopyUpdated.placements))) {
                        possibleSchemes.remove(previousScheme)
                        possibleSchemes.add(DigitScheme(previousSchemeCopyUpdated.placements))
                    }

                    i = 0
                    val previousSchemeReversedCopyUpdated = previousScheme.copy(placements = previousScheme.placements.clone())
                    placesToFill.reversed().forEach { place ->
                        previousSchemeReversedCopyUpdated.placements[place] = chars[i++]
                    }
                    if (!possibleSchemes.contains(DigitScheme(previousSchemeReversedCopyUpdated.placements))) {
                        possibleSchemes.remove(previousScheme)
                        possibleSchemes.add(DigitScheme(previousSchemeReversedCopyUpdated.placements))
                    }
                }
            }
        }


        return possibleSchemes.filter { !it.placements.any { it == Char.MIN_VALUE } }
    }

    fun part2(input: List<String>): Int {
        val entries = input.parseInput()
        val entryValues = mutableListOf<Int>()

        entries.forEach { entry ->
            //find right decoding scheme for current Entry
            val easyDigitPatterns = entry.patterns
                .filter { it.isEasyDigit() }
                .map { DigitPattern(it.toEasyDigit(), it) }
                .sortedBy { it.pattern.length }

            //build all possible schemes for easy digit patterns -> check if them are valid for all 10 digits

            val possibleSchemes = generateAllPossibleSchemes(easyDigitPatterns)

            val nonEasyDigitPatterns = entry.patterns.filter { !it.isEasyDigit() }

            var validScheme: DigitScheme? = null
            for (i in possibleSchemes.indices) {
                val schemeToCheck = possibleSchemes[i]

                //how to check if scheme is valid ?
                //go to each pattern and see if this pattern is one of the 10 in terms of placement
                var isValid = true
                for (digitPatternString in nonEasyDigitPatterns) {
                    val placementsList = digitPatternString.toCharArray().map {
                        schemeToCheck.placements.indexOf(it)
                    }.sorted()
                    if (!validDigitsPlaces.containsValue(placementsList)) {
                        isValid = false
                        break
                    }
                }
                if (isValid) {
                    validScheme = schemeToCheck
                    break
                }
            }

            if (validScheme == null) throw IllegalStateException("Valid Scheme Not Found - Abortion...")
            //decode value with valid scheme:
            val entryValue = entry.values.map { pattern ->
                val placementsForPattern = pattern.toCharArray().map { char ->
                    validScheme.placements.indexOfFirst { it == char }
                }.sorted()

                //find valid placement and get it's key
                validDigitsPlaces.filterValues { it == placementsForPattern }.keys.first()
            }.joinToString("").toInt()
            println("entry: ${entry.values.joinToString(" ")}: $entryValue")
            entryValues.add(entryValue)
        }

        return entryValues.sum()
    }


    println("****** Advent Of Code Day 08 ******")
    println("****** Clicking Seven Digit Displays  ******")

    val day = 8
    val dayString = if (day < 10) "0${day}" else "$day"
    val testInput = readInput("Day${dayString}_test", day)
    val expectedPart1 = 26
    val expectedPart2 = 61229
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

fun printScheme(schemes: List<DigitScheme>) {
    schemes.forEachIndexed { index, digitScheme ->

        val zeroChar = if (digitScheme.placements[0] == Char.MIN_VALUE) '.' else digitScheme.placements[0]
        val firstChar = if (digitScheme.placements[1] == Char.MIN_VALUE) '.' else digitScheme.placements[1]
        val secondChar = if (digitScheme.placements[2] == Char.MIN_VALUE) '.' else digitScheme.placements[2]
        val thirdChar = if (digitScheme.placements[3] == Char.MIN_VALUE) '.' else digitScheme.placements[3]
        val forthChar = if (digitScheme.placements[4] == Char.MIN_VALUE) '.' else digitScheme.placements[4]
        val fifthChar = if (digitScheme.placements[5] == Char.MIN_VALUE) '.' else digitScheme.placements[5]
        val sixChar = if (digitScheme.placements[6] == Char.MIN_VALUE) '.' else digitScheme.placements[6]

        println("Possible Schema $index")
        println(" $zeroChar$zeroChar$zeroChar ")
        println("$fifthChar   $firstChar")
        println("$fifthChar   $firstChar")
        println(" $sixChar$sixChar$sixChar ")
        println("$forthChar   $secondChar")
        println("$forthChar   $secondChar")
        println(" $thirdChar$thirdChar$thirdChar")

    }
}


