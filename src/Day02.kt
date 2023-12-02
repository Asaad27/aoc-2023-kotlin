const val MAX_REDS = 12
const val MAX_GREENS = 13
const val MAX_BLUES = 14
fun main() {
    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    check(part1(testInput).also { println(it) } == 8)
    check(part2(testInput).also { println(it) } == 2286)
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>): Int = input.asSequence()
    .map(String::toGame)
    .filter { game ->
        game.colorSets.all { it.isWithinLimits(MAX_REDS, MAX_GREENS, MAX_BLUES) }
    }.sumOf(Game::id)

fun part2(input: List<String>): Int = input.asSequence()
    .map(String::toGame)
    .sumOf { it.colorSets.calculatePower() }

fun List<ColorSet>.calculatePower(): Int = this.fold(listOf(0, 0, 0)) { (maxR, maxG, maxB), colorSet ->
    listOf(maxOf(maxR, colorSet.red), maxOf(maxG, colorSet.green), maxOf(maxB, colorSet.blue))
}.fold(1) { acc, el -> acc * el }

fun String.toGame(): Game {
    val (idPart, colorSetsPart) = this.split(":", limit = 2)
    val gameId = idPart.split(" ")[1].toInt()
    val colorSets = colorSetsPart.parseColorSets()
    return Game(gameId, colorSets)
}

fun String.parseColorSets(): List<ColorSet> = this.split(";")
    .map { colorSetString ->
        val colorMap = colorSetString.split(",")
            .associate {
                val (number, color) = it.trim().split(" ")
                color to number.toInt()
            }
        ColorSet(colorMap["blue"] ?: 0, colorMap["green"] ?: 0, colorMap["red"] ?: 0)
    }

fun ColorSet.isWithinLimits(maxReds: Int, maxGreens: Int, maxBlues: Int) =
    this.red <= maxReds && this.green <= maxGreens && this.blue <= maxBlues

data class Game(val id: Int, val colorSets: List<ColorSet>)
data class ColorSet(var blue: Int, var green: Int, var red: Int)