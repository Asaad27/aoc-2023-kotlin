fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { cardString ->
            val (_, numbersStr) = cardString.split(":").map(String::trim)
            val (winningNumbers, playerNumbers) = numbersStr.toTwoSets()

            val matches = playerNumbers.intersect(winningNumbers).size
            if (matches > 0) (1 shl (matches - 1)) else 0
        }

    }

    fun part2BruteForce(input: List<String>): Int {
        val cards = input.map { cardString ->
            val (cardNumberStr, numbersStr) = cardString.split(":").map(String::trim)
            val cardNumber = cardNumberStr.filter { it.isDigit() }.toInt() - 1

            val (winningNumbers, playerNumbers) = numbersStr.toTwoSets()
            Card(cardNumber, winningNumbers, playerNumbers)
        }

        var totalCards = input.size
        val queue = ArrayDeque<Card>().apply { addAll(cards) }

        while (queue.isNotEmpty()) {
            val card = queue.removeFirst()
            val matches = card.playerNumbers.intersect(card.winningNumbers).size

            for (i in 1..matches) {
                val nextCardIndex = card.index + i
                if (nextCardIndex < cards.size) {
                    queue.addLast(cards[nextCardIndex])
                    totalCards++
                }
            }
        }

        return totalCards
    }

    fun part2DP(input: List<String>): Int {
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

    fun part2SegmentTree(input: List<String>): Int {
        val cards = input.map { cardString ->
            val (_, numbersStr) = cardString.split(":").map(String::trim)
            val (winningNumbersStr, playerNumbersStr) = numbersStr.split("|").map(String::trim)
            val winningNumbers = winningNumbersStr.split(" ").filter(String::isNotEmpty).map(String::toInt).toSet()
            val playerNumbers = playerNumbersStr.split(" ").filter(String::isNotEmpty).map(String::toInt).toSet()
            winningNumbers to playerNumbers
        }

        val segmentTree = SegmentTree(input.size)
        for (i in cards.indices) {
            val (winningNumbers, playerNumbers) = cards[i]
            val matches = playerNumbers.intersect(winningNumbers).size

            if (matches > 0 && i + 1 < input.size) {
                segmentTree.update(i + 1, minOf(input.size - 1, i + matches), segmentTree.query(i, i))
            }
        }

        return input.indices.sumOf { segmentTree.query(it, it) }
    }



    val testInput = readInput("Day04_test")
    val input = readInput("Day04")

    check(part1(testInput) == 13)
    check(part2SegmentTree(testInput).also { println(it) } == 30)
    part1(input).println()

    val executionTimeSegmentTree = computeExecutionTime {
        part2SegmentTree(input)
    }

    val executionTimeDP = computeExecutionTime {
        part2DP(input)
    }

    val executionTimeBruteForce = computeExecutionTime {
        part2BruteForce(input)
    }

    println("brute force execution time: ${executionTimeBruteForce/1000000} ms")
    println("DP execution time: ${executionTimeDP/1000000} ms")
    println("segment tree execution time: ${executionTimeSegmentTree/1000000} ms")


}

fun computeExecutionTime(block: () -> Unit): Long {
    val startTime = System.nanoTime()
    block()
    return System.nanoTime() - startTime
}

data class Card(val index: Int, val winningNumbers: Set<Int>, val playerNumbers: Set<Int>)

fun String.toTwoSets(): Pair<Set<Int>, Set<Int>> {
    val (winningNumbersStr, playerNumbersStr) = this.split("|").map(String::trim)
    val winningNumbers = winningNumbersStr.split(" ").filter(String::isNotEmpty).map(String::toInt).toHashSet()
    val playerNumbers = playerNumbersStr.split(" ").filter(String::isNotEmpty).map(String::toInt).toHashSet()
    return winningNumbers to playerNumbers
}

class SegmentTree(private val size: Int) {
    private val tree = Array(size * 4) { 1 }

    private fun updateRange(node: Int, start: Int, end: Int, l: Int, r: Int, value: Int) {
        if (l > end || r < start) {
            return
        }

        if (start == end) {
            tree[node] += value
            return
        }

        val mid = (start + end) / 2
        updateRange(node * 2, start, mid, l, r, value)
        updateRange(node * 2 + 1, mid + 1, end, l, r, value)
        tree[node] = tree[node * 2] + tree[node * 2 + 1]
    }

    fun update(l: Int, r: Int, value: Int) {
        updateRange(1, 0, size - 1, l, r, value)
    }

    private fun query(node: Int, start: Int, end: Int, l: Int, r: Int): Int {
        if (l > end || r < start) {
            return 0
        }

        if (l <= start && end <= r) {
            return tree[node]
        }

        val mid = (start + end) / 2
        val p1 = query(node * 2, start, mid, l, r)
        val p2 = query(node * 2 + 1, mid + 1, end, l, r)
        return p1 + p2
    }

    fun query(l: Int, r: Int): Int {
        return query(1, 0, size - 1, l, r)
    }
}

