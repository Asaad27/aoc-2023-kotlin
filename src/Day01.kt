fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.first { c: Char -> c.isDigit() }.digitToInt() * 10 + it.last { c: Char -> c.isDigit() }.digitToInt()
        }
    }

    fun part2(input: List<String>): Int {
        val trie = Trie()
        trie.insert(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
            "1" to 1,
            "2" to 2,
            "3" to 3,
            "4" to 4,
            "5" to 5,
            "6" to 6,
            "7" to 7,
            "8" to 8,
            "9" to 9
        )
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
        } else {
            index++
        }
    }

    return (firstDigit ?: 0) * 10 + (lastDigit ?: 0)
}


class TrieNode(var isWord: Boolean = false, var value: Int? = null) {
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
            current.isWord = true
            current.value = value
        }
    }

    fun findNumberInString(str: String, startIndex: Int): Int {
        var current = root
        for (i in startIndex until str.length) {
            current = current.children[str[i]] ?: return 0
            if (current.isWord) return current.value!!
        }

        return 0
    }
}
