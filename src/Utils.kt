import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, day: Int) = File("src/day${dayString(day)}", "$name.txt").readLines()

private fun dayString(day: Int) = if (day < 10) "0$day" else day

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
