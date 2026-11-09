package it.srik.TypeQ25.inputmethod

import org.junit.Assert.assertEquals
import org.junit.Test

class TelexEngineTest {

    /** Types [keys] one by one the way the IME does: a key either rewrites the word or is appended. */
    private fun type(keys: String): String {
        var word = ""
        for (k in keys) {
            word = TelexEngine.applyKey(word, k) ?: (word + k)
        }
        return word
    }

    @Test
    fun marks() {
        assertEquals("â", type("aa"))
        assertEquals("ê", type("ee"))
        assertEquals("ô", type("oo"))
        assertEquals("ă", type("aw"))
        assertEquals("ơ", type("ow"))
        assertEquals("ư", type("uw"))
        assertEquals("ư", type("w"))
        assertEquals("đi", type("ddi"))
        assertEquals("đi", type("did"))
        assertEquals("trương", type("truowng"))
        assertEquals("trương", type("truongw"))
    }

    @Test
    fun tones() {
        assertEquals("việt", type("vieetj"))
        assertEquals("việt", type("vietje"))
        assertEquals("tiếng", type("tieesng"))
        assertEquals("Việt", type("Vieetj"))
        assertEquals("hòa", type("hoaf"))
        assertEquals("hoàng", type("hoangf"))
        assertEquals("thủy", type("thuyr"))
        assertEquals("người", type("nguwowif"))
        assertEquals("người", type("nguoiwf"))
        assertEquals("quả", type("quar"))
        assertEquals("già", type("giaf"))
        assertEquals("gì", type("gif"))
        assertEquals("ngoài", type("ngoaif"))
        assertEquals("khuyến", type("khuyeens"))
        assertEquals("của", type("cuar"))
        assertEquals("cửa", type("cuwar"))
        assertEquals("quốc", type("quoocs"))
        assertEquals("ăn", type("awn"))
        assertEquals("hoặc", type("hoawcj"))
        assertEquals("hoặc", type("hoacwj"))
        assertEquals("hoặc", type("hoacjw"))
        assertEquals("ngoặc", type("ngoawcj"))
        assertEquals("xoắn", type("xoawns"))
        assertEquals("mưa", type("muaw"))
        assertEquals("mới", type("moiws"))
    }

    @Test
    fun toneMovesWhenMarkChanges() {
        assertEquals("người", type("nguofiw"))
        assertEquals("việt", type("viejte"))
    }

    @Test
    fun undoTypesTheKey() {
        assertEquals("as", type("ass"))
        assertEquals("aa", type("aaa"))
        assertEquals("w", type("ww"))
        assertEquals("ww", type("www"))
        assertEquals("W", type("Ww"))
        assertEquals("thuw", type("thuww"))
        assertEquals("dd", type("ddd"))
        assertEquals("baz", type("baz"))
        assertEquals("ba", type("basz"))
    }

    @Test
    fun keepsCase() {
        assertEquals("VIỆT", type("VIEETJ"))
        assertEquals("Đi", type("Ddi"))
        assertEquals("Ư", type("W"))
    }

    @Test
    fun ignoresNonVietnameseWords() {
        assertEquals("fixs", type("fixs"))
        assertEquals("http", type("http"))
        // stop finals only take sắc / nặng
        assertEquals("catf", type("catf"))
        assertEquals("cát", type("cats"))
    }
}
