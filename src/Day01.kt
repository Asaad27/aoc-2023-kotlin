fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.first { c: Char -> c.isDigit() }.digitToInt() * 10 + it.last { c: Char -> c.isDigit() }.digitToInt()
        }
    }

    fun part2(input: List<String>): Int {
        val trie = Trie()
        val numberWords = mapOf(
            "one" to 1, "two" to 2, "three" to 3, "four" to 4,
            "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
        )
        numberWords.forEach { (word, value) ->
            trie.insert(word to value)
            if (value < 10) trie.insert(value.toString() to value)
        }

        return input.sumOf {
            it.extractNumber(trie)
        }
    }

    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    //check(part2(testInput).also { println(it) } == 281)
    part1(input).println()
    part2(input).println()
}

fun String.extractNumber(trie: Trie): Int {
    var index = 0
    var firstDigit: Int? = null
    var lastDigit: Int? = null

    while (index < length) {
        val digit = trie.findNumberInString(this, index)
        if (digit != 0) {
            firstDigit = firstDigit ?: digit
            lastDigit = digit
            index += digit.toString().length
            continue
        }
        index++
    }

    return (firstDigit ?: 0) * 10 + (lastDigit ?: 0)
}


class TrieNode(var isDigit: Boolean = false, var value: Int? = null) {
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
}

class Trie {
    private val root = TrieNode()

    fun insert(vararg numbers: Pair<String, Int>) {
        numbers.forEach { (word, value) ->
            var current = root
            for (ch in word) {
                current = current.children.computeIfAbsent(ch) { TrieNode() }
            }
            current.isDigit = true
            current.value =  value
        }
    }

    fun findNumberInString(str: String, startIndex: Int): Int {
        var current = root
        for (i in startIndex until str.length) {
            current = current.children[str[i]] ?: return 0
            if (current.isDigit) return current.value!!
        }

        return 0
    }
}
