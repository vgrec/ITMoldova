package com.itmoldova.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class HtmlParser(private val displayWidth: Int,
                 private val displayDensity: Float,
                 private val bgColor: Int,
                 private val textColor: Int,
                 private val linkColor: Int) {

    var content: String = ""
    var headerUrl: String? = ""

    fun parse(html: String): String {
        val header =
                "<!doctype html>" +
                        "<html>" +
                        "<meta charset=\"utf-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width; user-scalable=no; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0; target-densityDpi=device-dpi;\"" +
                        "<head>" +
                        getBodyStyle() +
                        getImageStyle() +
                        getFrameStyle() +
                        "</head>"

        val body = "<body>$html</body>"

        val footer = "</html>"

        val sb = StringBuilder()
        sb.append(header)
        sb.append(parseBody(body))
        sb.append(footer)

        val text = sb.toString()
        content = text
        return text
    }

    fun getHeaderImageUrl(): String? {
        return headerUrl
    }

    private fun parseBody(body: String): String {
        val document = Jsoup.parse(body)
        val images = document.select("img[src]")

        val first = images.first()
        first?.let {
            headerUrl = it.absUrl("src")
            it.remove()
        }

        for (i in 0 until images.size) {
            val image = images[i]
            if (isImageSizeMissing(image)) {
                continue
            }

            val dimens = calculateImageHeight(Integer.valueOf(image.attr("width")), Integer.valueOf(image.attr("height")))
            image.attr("width", dimens[0].toString())
            image.attr("height", dimens[1].toString())
        }

        return document.toString()
    }

    private fun isImageSizeMissing(image: Element): Boolean {
        return try {
            Integer.valueOf(image.attr("width"))
            Integer.valueOf(image.attr("height"))
            false
        } catch (e: NumberFormatException) {
            true
        }
    }

    private fun calculateImageHeight(frameX: Int, frameY: Int): IntArray {
        val webWidth = displayWidth / displayDensity
        val webHeight = if (webWidth < frameX) {
            val ration = frameX / webWidth
            frameY / ration
        } else {
            val ration = webWidth / frameX
            frameY * ration
        }

        return intArrayOf(webWidth.toInt(), webHeight.toInt())
    }


    private fun getFrameStyle(): String {
        val webWidth = displayWidth / displayDensity
        val frameX = 640f
        val frameY = 390f
        val webHeight = if (webWidth < frameX) {
            val ration = frameX / webWidth
            frameY / ration
        } else {
            val ration = webWidth / frameX
            frameY * ration
        }

        return "<style>iframe {display: block; max-width:100%; width:" + webWidth + "px; height:" + webHeight + "px; margin-top:10px; margin-bottom:10px; }</style>"
    }

    private fun getImageStyle(): String = "<style>img {display: inline; max-width: 100%; }</style>"

    private fun getBodyStyle(): String = "<style>" +
            "body {color: " + toHex(textColor) + "; padding:0; margin:0; background-color: " + toHex(bgColor) + ";} " +
            "a { color: " + toHex(linkColor) + "; }" +
            "</style>"

    private fun toHex(color: Int) = String.format("#%06X", 0xFFFFFF and color)
}