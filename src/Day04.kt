fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { cardString ->
            val (_, numbersStr) = cardString.split(":").map(String::trim)
            val (winningNumbers, playerNumbers) = numbersStr.toTwoSets()

            val matches = playerNumbers.intersect(winningNumbers).size
            if (matches > 0) (1 shl (matches - 1)) else 0
        }

    }

    fun part2(input: List<String>): Int {
        val cards = input.map { cardString ->
            val (_, numbersStr) = cardString.split(":").map(String::trim)
            val (winningNumbers, playerNumbers) = numbersStr.toTwoSets()
            winningNumbers to playerNumbers
        }

        val dp = IntArray(input.size) { 1 }

        for (i in cards.indices) {
            val (winningNumbers, playerNumbers) = cards[i]
            val matches = playerNumbers.intersect(winningNumbers).size

            for (j in 1..matches) {
                if (i + j < dp.size) {
                    dp[i + j] += dp[i]
                }
            }
        }


        return dp.sum()
    }


    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 13)
    check(part2(testInput).also { println(it) } == 30)
    part1(input).println()
    part2(input).println()
}

data class Card(val index: Int, val winningNumbers: Set<Int>, val playerNumbers: Set<Int>)
fun String.toTwoSets(): Pair<Set<Int>, Set<Int>> {
    val (winningNumbersStr, playerNumbersStr) = this.split("|").map(String::trim)
    val winningNumbers = winningNumbersStr.split(" ").filter(String::isNotEmpty).map(String::toInt).toHashSet()
    val playerNumbers = playerNumbersStr.split(" ").filter(String::isNotEmpty).map(String::toInt).toHashSet()
    return winningNumbers to playerNumbers
}

