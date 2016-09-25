package com.itmoldova.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class parses the HTML content of an article and splits it into a list of {@link Block}s.
 * Then based on this list, the appropriate views are created in the UI.
 */
public class ContentParser {
    private static final String IMG_START_TAG = "<img";
    private static final String IMG_END_TAG = "/>";
    private static final String SRC_START_TAG = "src=\"";
    private static final String SRC_END_TAG = "\"";


    public static List<Block> parse(String content) {
        List<Block> blocks = new ArrayList<>();

        int startTag;
        while ((startTag = content.indexOf(IMG_START_TAG)) != -1) {
            int endTag = content.indexOf(IMG_END_TAG, startTag);
            if (endTag != -1) {
                String text = content.substring(0, startTag);
                if (text.length() > 0) {
                    blocks.add(new Block(Block.Type.TEXT, text));
                }

                String imageTag = content.substring(startTag, endTag + IMG_END_TAG.length());
                String imageUrl = extractUrl(imageTag);
                blocks.add(new Block(Block.Type.IMAGE, imageUrl));
                content = content.substring(endTag + IMG_END_TAG.length(), content.length());
            }
        }

        // Add the remaining text (if any)
        if (content.length() > 0) {
            blocks.add(new Block(Block.Type.TEXT, content));
        }

        // TODO: normalize the list, for now it is hardcoded just for testing.
        blocks.remove(0);

        return blocks;
    }

    public static String extractUrl(String imageTag) {
        String url = null;
        int startTag = imageTag.indexOf(SRC_START_TAG);
        if (startTag != -1) {
            int endTag = imageTag.indexOf(SRC_END_TAG, startTag + SRC_START_TAG.length());
            if (endTag != -1) {
                url = imageTag.substring(startTag + SRC_START_TAG.length(), endTag);
            }
        }

        return url;
    }

    public static String extractFirstImage(String content) {
        if (content == null) {
            return null;
        }

        int imgTagIndex = content.indexOf(IMG_START_TAG);
        if (imgTagIndex != -1) {
            return extractUrl(content.substring(imgTagIndex));
        } else {
            return null;
        }
    }
}
