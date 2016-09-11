package com.itmoldova;

import com.itmoldova.parser.Block;
import com.itmoldova.parser.ContentParser;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ContentParser} class
 */
public class ContentParserTest {

    private static final String TEXT_1 = "<p>De fiecare dată când se dezvoltă o noua linie de smartphone-uri Nexus, Google vine cu un nume într-un fel sau altul asociat cu vietățile marine.";
    private static final String TEXT_2 = "<p><a href=\"http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-2.png\">";
    private static final String TEXT_3 = "</a><p style=\"text-align: justify;\"><span class=\"notranslate\"> Gadgetul dispune de un procesor MediaTek Helio P10";

    private static final String IMAGE_1 = "http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-2-403x1024.png";
    private static final String IMAGE_2 = "http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-3-396x1024.png";
    private static final String IMAGE_3 = "http://itmoldova.com/wp-content/uploads/2016/08/Meizu-M3E-5-615x591.png";

    private static final String IMAGE_TAG_1 = "<img class=\"aligncenter size-large wp-image-110474\" src=\"" + IMAGE_1 + "\" alt=\"Meizu M3E 2\" width=\"403\" height=\"1024\" />";
    private static final String IMAGE_TAG_2 = "<img class=\"aligncenter size-large wp-image-110475\" src=\"" + IMAGE_2 + "\" alt=\"Meizu M3E 3\" width=\"396\" height=\"1024\" />";
    private static final String IMAGE_TAG_3 = "<img class=\"aligncenter size-large wp-image-110476\" src=\"" + IMAGE_3 + "\" alt=\"Meizu M3E 5\" width=\"615\" height=\"591\" />";

    private static final String CONTENT_ONLY_TEXT = TEXT_1;
    private static final String CONTENT_IMAGE_FIRST_TEXT_AFTER = IMAGE_TAG_1 + TEXT_1;
    private static final String CONTENT_TEXT_FIRST_IMAGE_AFTER = TEXT_1 + IMAGE_TAG_1;

    private static final String CONTENT_MIXED = TEXT_1
            + IMAGE_TAG_1
            + TEXT_2
            + IMAGE_TAG_2
            + TEXT_3
            + IMAGE_TAG_3;

    @Test
    public void testExtractUrl() {
        String url = ContentParser.extractUrl(IMAGE_TAG_1);
        assertThat(url).isEqualTo(IMAGE_1);
    }

    @Test
    public void testExtractFirstImageUrlFromContent(){
        String url = ContentParser.extractFirstImage(CONTENT_MIXED);
        assertThat(url).isEqualTo(IMAGE_1);
    }

    @Test
    public void testContentParser_OnlyText() {
        List<Block> parts = ContentParser.parse(CONTENT_ONLY_TEXT);
        assertThat(parts.size()).isEqualTo(1);
        assertThat(parts.get(0).getContent()).isEqualTo(TEXT_1);
    }

    @Test
    public void testContentParser_ImageFirstTextAfter() {
        List<Block> parts = ContentParser.parse(CONTENT_IMAGE_FIRST_TEXT_AFTER);
        assertThat(parts.size()).isEqualTo(2);
        assertThat(parts.get(0).getContent()).isEqualTo(IMAGE_1);
        assertThat(parts.get(1).getContent()).isEqualTo(TEXT_1);
    }

    @Test
    public void testContentParser_TextFirstImageAfter() {
        List<Block> parts = ContentParser.parse(CONTENT_TEXT_FIRST_IMAGE_AFTER);
        assertThat(parts.size()).isEqualTo(2);
        assertThat(parts.get(0).getContent()).isEqualTo(TEXT_1);
        assertThat(parts.get(1).getContent()).isEqualTo(IMAGE_1);
    }

    @Test
    public void testContentParser_Mixed() {
        List<Block> parts = ContentParser.parse(CONTENT_MIXED);
        assertThat(parts.size()).isEqualTo(6);
        assertThat(parts.get(0).getContent()).isEqualTo(TEXT_1);
        assertThat(parts.get(1).getContent()).isEqualTo(IMAGE_1);
        assertThat(parts.get(2).getContent()).isEqualTo(TEXT_2);
        assertThat(parts.get(3).getContent()).isEqualTo(IMAGE_2);
        assertThat(parts.get(4).getContent()).isEqualTo(TEXT_3);
        assertThat(parts.get(5).getContent()).isEqualTo(IMAGE_3);
    }
}
