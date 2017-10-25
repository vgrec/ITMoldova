package com.itmoldova.parser

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Unit tests for [ContentParser] class
 */
class ContentParserTest {

    @Test
    fun testExtractUrl() {
        val url = ContentParser.extractUrl(IMAGE_TAG_1)
        assertThat(url).isEqualTo(IMAGE_1)
    }

    @Test
    fun testExtractFirstImageUrlFromContent() {
        val url = ContentParser.extractFirstImage(CONTENT_MIXED)
        assertThat(url).isEqualTo(IMAGE_1)
    }

    @Test
    fun testContentParser_OnlyText() {
        val parts = ContentParser(CONTENT_ONLY_TEXT).parse()
        assertThat(parts.size).isEqualTo(1)
        assertThat(parts[0].content).isEqualTo(TEXT_1)
    }

    @Test
    fun testContentParser_ImageFirstTextAfter() {
        val parts = ContentParser(CONTENT_IMAGE_FIRST_TEXT_AFTER).parse()
        assertThat(parts.size).isEqualTo(2)
        assertThat(parts[0].content).isEqualTo(IMAGE_1)
        assertThat(parts[1].content).isEqualTo(TEXT_1)
    }

    @Test
    fun testContentParser_TextFirstImageAfter() {
        val parts = ContentParser(CONTENT_TEXT_FIRST_IMAGE_AFTER).parse()
        assertThat(parts.size).isEqualTo(2)
        assertThat(parts[0].content).isEqualTo(TEXT_1)
        assertThat(parts[1].content).isEqualTo(IMAGE_1)
    }

    @Test
    fun testContentParser_Mixed() {
        val parts = ContentParser(CONTENT_MIXED).parse()
        assertThat(parts.size).isEqualTo(6)
        assertThat(parts[0].content).isEqualTo(TEXT_1)
        assertThat(parts[1].content).isEqualTo(IMAGE_1)
        assertThat(parts[2].content).isEqualTo(TEXT_2)
        assertThat(parts[3].content).isEqualTo(IMAGE_2)
        assertThat(parts[4].content).isEqualTo(TEXT_3)
        assertThat(parts[5].content).isEqualTo(IMAGE_3)
    }

    companion object {

        private val TEXT_1 = "<p>De fiecare dată când se dezvoltă o noua linie de smartphone-uri Nexus, Google vine cu un nume într-un fel sau altul asociat cu vietățile marine."
        private val TEXT_2 = "<p><a href=\"http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-2.png\">"
        private val TEXT_3 = "</a><p style=\"text-align: justify;\"><span class=\"notranslate\"> Gadgetul dispune de un procesor MediaTek Helio P10"

        private val IMAGE_1 = "http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-2-403x1024.png"
        private val IMAGE_2 = "http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-3-396x1024.png"
        private val IMAGE_3 = "http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-5-615x591.png"

        private val IMAGE_TAG_1 = "<img class=\"aligncenter size-large wp-image-110474\" src=\"$IMAGE_1\" alt=\"Meizu M3E 2\" width=\"403\" height=\"1024\" />"
        private val IMAGE_TAG_2 = "<img class=\"aligncenter size-large wp-image-110475\" src=\"$IMAGE_2\" alt=\"Meizu M3E 3\" width=\"396\" height=\"1024\" />"
        private val IMAGE_TAG_3 = "<img class=\"aligncenter size-large wp-image-110476\" src=\"$IMAGE_3\" alt=\"Meizu M3E 5\" width=\"615\" height=\"591\" />"

        private val CONTENT_ONLY_TEXT = TEXT_1
        private val CONTENT_IMAGE_FIRST_TEXT_AFTER = IMAGE_TAG_1 + TEXT_1
        private val CONTENT_TEXT_FIRST_IMAGE_AFTER = TEXT_1 + IMAGE_TAG_1

        private val CONTENT_MIXED = TEXT_1 + IMAGE_TAG_1 + TEXT_2 + IMAGE_TAG_2 + TEXT_3 + IMAGE_TAG_3
    }
}
