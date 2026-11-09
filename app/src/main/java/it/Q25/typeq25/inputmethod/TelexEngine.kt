package it.srik.TypeQ25.inputmethod

/**
 * Vietnamese Telex, applied to the word that sits before the cursor.
 *
 * [applyKey] returns what the word should become after typing [key], or null when
 * the key is not a Telex modifier for that word and should be typed as a normal letter.
 *
 * Keys: aa=â ee=ê oo=ô aw=ă ow=ơ uw=ư uow=ươ w=ư dd=đ,
 * tones s(sắc) f(huyền) r(hỏi) x(ngã) j(nặng), z removes the tone.
 * Repeating a modifier undoes it and types the key (e.g. "vieets" + s -> "viets").
 * Tones follow the traditional placement (hòa, thủy).
 */
object TelexEngine {

    private const val BASES = "aăâeêioôơuưy"
    private const val UNDO = '?'

    // Row per base vowel: no tone, sắc, huyền, hỏi, ngã, nặng
    private val TONED = arrayOf(
        "aáàảãạ", "ăắằẳẵặ", "âấầẩẫậ",
        "eéèẻẽẹ", "êếềểễệ",
        "iíìỉĩị",
        "oóòỏõọ", "ôốồổỗộ", "ơớờởỡợ",
        "uúùủũụ", "ưứừửữự",
        "yýỳỷỹỵ"
    )

    private val TONE_KEYS = mapOf('s' to 1, 'f' to 2, 'r' to 3, 'x' to 4, 'j' to 5)

    private val INITIALS = setOf(
        "", "b", "c", "ch", "d", "đ", "g", "gh", "gi", "h", "k", "kh", "l", "m", "n",
        "ng", "ngh", "nh", "p", "ph", "qu", "r", "s", "t", "th", "tr", "v", "x"
    )

    private val FINALS = setOf("", "c", "ch", "m", "n", "ng", "nh", "p", "t")

    // Finals that only allow sắc or nặng
    private val STOP_FINALS = setOf("c", "ch", "p", "t")

    private data class Vowel(val base: Char, val tone: Int)

    private fun decompose(c: Char): Vowel? {
        for (row in TONED) {
            val i = row.indexOf(c)
            if (i >= 0) return Vowel(row[0], i)
        }
        return null
    }

    private fun compose(base: Char, tone: Int): Char = TONED[BASES.indexOf(base)][tone]

    private class Syllable(
        val chars: CharArray,     // lowercase word
        val initialEnd: Int,      // vowel cluster starts here
        val vowelEnd: Int,        // final consonant starts here
    ) {
        val initial get() = String(chars, 0, initialEnd)
        val final get() = String(chars, vowelEnd, chars.size - vowelEnd)
        val vowelRange get() = initialEnd until vowelEnd
    }

    /** Splits a lowercase word into initial / vowel cluster / final, or null if it isn't one Vietnamese syllable. */
    private fun parse(word: String): Syllable? {
        val chars = word.toCharArray()
        var start = chars.indexOfFirst { decompose(it) != null }
        if (start < 0) {
            return if (word in INITIALS) Syllable(chars, chars.size, chars.size) else null
        }
        var end = start
        while (end < chars.size && decompose(chars[end]) != null) end++
        if ((end until chars.size).any { decompose(chars[it]) != null }) return null

        // "qu" and "gi" swallow their u / i when another vowel follows
        val first = decompose(chars[start])!!.base
        if (end - start > 1 &&
            ((word.substring(0, start) == "q" && first == 'u') ||
                (word.substring(0, start) == "g" && first == 'i'))
        ) {
            start++
        }

        if (end - start > 3) return null
        val syllable = Syllable(chars, start, end)
        if (syllable.initial !in INITIALS || syllable.final !in FINALS) return null
        return syllable
    }

    fun applyKey(word: String, key: Char): String? {
        val k = key.lowercaseChar()
        val lower = word.lowercase()
        val result = when (k) {
            in TONE_KEYS -> applyTone(lower, TONE_KEYS.getValue(k))
            'z' -> removeTone(lower)
            'a', 'e', 'o' -> applyCircumflex(lower, k)
            'w' -> applyHorn(lower)
            'd' -> applyStroke(lower)
            else -> null
        } ?: return null

        // A trailing UNDO marks "modifier undone, type the key itself"
        val undo = result.last() == UNDO
        return buildString {
            for (i in result.indices) {
                val c = result[i]
                val upper = if (i < word.length) word[i].isUpperCase() else key.isUpperCase()
                append(if (upper) c.uppercaseChar() else c)
            }
        }.let { if (undo) it.substring(0, it.length - 1) + key else it }
    }

    private fun currentTone(s: Syllable): Int =
        s.vowelRange.maxOfOrNull { decompose(s.chars[it])!!.tone } ?: 0

    private fun stripTones(s: Syllable) {
        for (i in s.vowelRange) {
            val v = decompose(s.chars[i])!!
            s.chars[i] = compose(v.base, 0)
        }
    }

    private fun applyTone(word: String, tone: Int): String? {
        val s = parse(word) ?: return null
        if (s.vowelRange.isEmpty()) return null
        val current = currentTone(s)
        stripTones(s)
        if (current == tone) {
            return String(s.chars) + UNDO
        }
        if (s.final in STOP_FINALS && tone != 1 && tone != 5) return null
        val pos = tonePosition(s)
        s.chars[pos] = compose(decompose(s.chars[pos])!!.base, tone)
        return String(s.chars)
    }

    private fun removeTone(word: String): String? {
        val s = parse(word) ?: return null
        if (currentTone(s) == 0) return null
        stripTones(s)
        return String(s.chars)
    }

    private fun tonePosition(s: Syllable): Int {
        val range = s.vowelRange
        val bases = range.map { decompose(s.chars[it])!!.base }
        val horn = bases.indexOf('ơ')
        if (horn >= 0) return range.first + horn
        val marked = bases.indexOfFirst { it in "ăâêôư" }
        if (marked >= 0) return range.first + marked
        if (s.final.isNotEmpty()) return range.last
        return when (bases.size) {
            1, 2 -> range.first
            else -> range.first + 1
        }
    }

    /** Re-tones the cluster after a mark change, since the right tone position can move (e.g. "hoà" vs "huệ"). */
    private fun replaceVowel(s: Syllable, index: Int, newBase: Char) {
        val tone = currentTone(s)
        stripTones(s)
        s.chars[index] = newBase
        if (tone != 0) {
            val pos = tonePosition(s)
            s.chars[pos] = compose(decompose(s.chars[pos])!!.base, tone)
        }
    }

    private fun applyCircumflex(word: String, k: Char): String? {
        val s = parse(word) ?: return null
        val hat = when (k) { 'a' -> 'â'; 'e' -> 'ê'; else -> 'ô' }
        val variants = when (k) { 'a' -> "aă"; 'e' -> "e"; else -> "oơ" }
        for (i in s.vowelRange) {
            val base = decompose(s.chars[i])!!.base
            if (base == hat) {
                replaceVowel(s, i, k)
                return String(s.chars) + UNDO
            }
        }
        for (i in s.vowelRange) {
            if (decompose(s.chars[i])!!.base in variants) {
                replaceVowel(s, i, hat)
                return String(s.chars)
            }
        }
        return null
    }

    private fun applyHorn(word: String): String? {
        val s = parse(word) ?: return null
        val range = s.vowelRange
        if (range.isEmpty()) {
            return if (s.final.isEmpty()) word + "ư" else null
        }
        val bases = range.map { decompose(s.chars[it])!!.base }

        // u + o -> ươ
        for (j in 0 until bases.size - 1) {
            if (bases[j] in "uư" && bases[j + 1] in "oơô" && !(bases[j] == 'ư' && bases[j + 1] == 'ơ')) {
                replaceVowel(s, range.first + j, 'ư')
                replaceVowel(s, range.first + j + 1, 'ơ')
                return String(s.chars)
            }
        }
        // o + a -> oă (hoặc, xoắn): "ơa" is not Vietnamese
        for (j in 0 until bases.size - 1) {
            if (bases[j] == 'o' && bases[j + 1] in "aâ") {
                replaceVowel(s, range.first + j + 1, 'ă')
                return String(s.chars)
            }
        }
        for ((from, to) in listOf('o' to 'ơ', 'ô' to 'ơ', 'u' to 'ư', 'a' to 'ă', 'â' to 'ă')) {
            val j = bases.indexOf(from)
            if (j >= 0) {
                replaceVowel(s, range.first + j, to)
                return String(s.chars)
            }
        }
        // A lone ư came from a bare w: a second w means a literal "w"
        if (word == "ư") return "w"

        // Nothing left to convert: undo existing horns/breves
        var changed = false
        for ((j, b) in bases.withIndex()) {
            val plain = when (b) { 'ơ' -> 'o'; 'ư' -> 'u'; 'ă' -> 'a'; else -> null } ?: continue
            replaceVowel(s, range.first + j, plain)
            changed = true
        }
        return if (changed) String(s.chars) + UNDO else null
    }

    private fun applyStroke(word: String): String? {
        val s = parse(word) ?: return null
        return when (s.initial) {
            "d" -> "đ" + word.substring(1)
            "đ" -> "d" + word.substring(1) + UNDO
            else -> null
        }
    }
}
