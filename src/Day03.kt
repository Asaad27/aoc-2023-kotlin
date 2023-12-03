
fun main() {
    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    fun part1(input: List<String>): Int {
        val symbols = findSymbols(input)
        return symbols.sumOf { sumAdjacentNumbers(input, it, HashSet()).first }
    }

    fun part2(input: List<String>): Int {
        val symbols = findSymbols(input)
        return symbols.sumOf { sumAdjacentNumbers(input, it, HashSet()).second }
    }

    println("Test Part 1: ${part1(testInput)}")
    println("Test Part 2: ${part2(testInput)}")

    println("Result Part 1: ${part1(input)}")
    println("Result Part 2: ${part2(input)}")
}

fun findSymbols(input: List<String>): List<Pair<Int, Int>> =
    input.flatMapIndexed { i, row ->
        row.mapIndexedNotNull { j, char -> if (char != '.' && !char.isDigit()) i to j else null }
    }

fun sumAdjacentNumbers(input: List<String>, symbol: Pair<Int, Int>, countedNumbers: HashSet<Pair<Int, Int>>): Pair<Int, Int> {
    val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1, -1 to -1, 1 to 1, -1 to 1, 1 to -1)
    val numbers = mutableListOf<String>()

    directions.map { (di, dj) -> symbol.first + di to symbol.second + dj }
        .filter { (i, j) -> isValidDigitPosition(input, i, j, countedNumbers) }
        .forEach { (i, j) ->
            if (countedNumbers.contains(i to j)) return@forEach
            val number = extractNumber(input, i, j, countedNumbers)
            numbers.add(number.toString())
        }

    val sum = numbers.sumOf { it.toInt() }
    val product = if (numbers.size == 2) numbers[0].toInt() * numbers[1].toInt() else 0

    return sum to product
}

fun isValidDigitPosition(input: List<String>, i: Int, j: Int, countedNumbers: HashSet<Pair<Int, Int>>) =
    i in input.indices && j in input[i].indices && input[i][j].isDigit() && i to j !in countedNumbers

fun extractNumber(input: List<String>, i: Int, j: Int, countedNumbers: HashSet<Pair<Int, Int>>): Int {
    val numberStr = buildString {
        var leftIndex = j
        while (leftIndex >= 0 && input[i][leftIndex].isDigit()) {
            insert(0, input[i][leftIndex])
            countedNumbers.add(i to leftIndex)
            leftIndex--
        }

        var rightIndex = j + 1
        while (rightIndex in input[i].indices && input[i][rightIndex].isDigit()) {
            append(input[i][rightIndex])
            countedNumbers.add(i to rightIndex)
            rightIndex++
        }
    }

    return numberStr.toIntOrNull() ?: 0
}
