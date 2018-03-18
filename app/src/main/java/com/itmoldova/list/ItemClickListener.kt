package com.itmoldova.list

import com.itmoldova.model.Article

/**
 * Author vgrec, on 23.07.16.
 */
interface ItemClickListener {
    fun onItemClicked(article: Article)
}
