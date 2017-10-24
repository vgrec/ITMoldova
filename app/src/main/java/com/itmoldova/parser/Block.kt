package com.itmoldova.parser

/**
 * <pre>
 * A [Block] is a piece of information from the HTML content of an article.
 *
 * eg.:
 * Imagine that the content of an article is structured as follows:
 * 1) some introductory text,
 * 2) then an image follows,
 * 3) then again some text.
 *
 * The above content can be represented by 3 [Block]s:
 * first block would be of type TEXT, second block of type IMAGE, and third again of type TEXT.
</pre> *
 */
class Block(val type: Type, val content: String) {

    enum class Type {
        IMAGE,
        TEXT,
        VIDEO
    }

    override fun toString(): String = "\n\n" + type.toString() + ":\n" + content
}
