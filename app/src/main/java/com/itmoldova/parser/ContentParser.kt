package com.itmoldova.parser

import android.text.Html
import android.text.TextUtils
import java.util.*

/**
 * This class parses the HTML content of an article and splits it into a list of [Block]s.
 * Then based on this list, the appropriate views are created in the UI.
 */
class ContentParser(private var content: String) {
    private val blocks = ArrayList<Block>()

    fun parse(): MutableList<Block> {
        var startTag = 0
        while ({ startTag = content.indexOf(IMG_START_TAG); startTag }() != -1) {
            val endTag = content.indexOf(IMG_END_TAG, startTag)
            if (endTag != -1) {
                val text = content.substring(0, startTag)
                if (text.isNotEmpty()) {
                    blocks.add(Block(Block.Type.TEXT, text))
                }

                val imageTag = content.substring(startTag, endTag + IMG_END_TAG.length)
                val imageUrl = extractUrl(imageTag)
                blocks.add(Block(Block.Type.IMAGE, imageUrl!!))
                content = content.substring(endTag + IMG_END_TAG.length, content.length)
            }
        }

        // Add the remaining text (if any)
        if (content.isNotEmpty()) {
            blocks.add(Block(Block.Type.TEXT, content))
        }

        return blocks
    }

    fun normalize(blocks: MutableList<Block>): MutableList<Block> {
        if (blocks[0].type === Block.Type.TEXT) {
            // Convert the html content to String,
            // if empty string is returned then the block is removed.
            val content = Html.fromHtml(blocks[0].content).toString()
            if (TextUtils.isEmpty(content)) {
                blocks.removeAt(0)
            }
        }
        return blocks
    }

    fun getHeaderImageFromBlocks(blocks: MutableList<Block>): String? {
        return if (blocks[0].type === Block.Type.IMAGE) {
            blocks.removeAt(0).content
        } else null
    }

    companion object {
        private val IMG_START_TAG = "<img"
        private val IMG_END_TAG = "/>"
        private val SRC_START_TAG = "src=\""
        private val SRC_END_TAG = "\""

        fun extractUrl(imageTag: String): String? {
            var url: String? = null
            val startTag = imageTag.indexOf(SRC_START_TAG)
            if (startTag != -1) {
                val endTag = imageTag.indexOf(SRC_END_TAG, startTag + SRC_START_TAG.length)
                if (endTag != -1) {
                    url = imageTag.substring(startTag + SRC_START_TAG.length, endTag)
                }
            }

            return url
        }

        fun extractFirstImage(content: String?): String? {
            if (content == null) {
                return null
            }

            val imgTagIndex = content.indexOf(IMG_START_TAG)
            return if (imgTagIndex != -1) {
                extractUrl(content.substring(imgTagIndex))
            } else {
                null
            }
        }
    }
}
