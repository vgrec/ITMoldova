package com.itmoldova.parser;

/**
 * <pre>
 * A {@link Block} is a piece of information from the HTML content of an article.
 *
 * eg.:
 * Imagine that the content of an article is structured as follows:
 *    1) some introductory text,
 *    2) then an image follows,
 *    3) then again some text.
 *
 * The above content can be represented by 3 {@link Block}s:
 * first block would be of type TEXT, second block of type IMAGE, and third again of type TEXT.
 * </pre>
 */
public class Block {
    Type type;
    String content;

    public Block(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public enum Type {
        IMAGE,
        TEXT,
        VIDEO
    }
}
