package com.itmoldova.bookmarks


import android.app.Fragment
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.itmoldova.R


/**
 * A simple [Fragment] subclass.
 */
class BookmarksFragment : Fragment(), BookmarksContract.View {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_bookmarks, container, false)
        return view
    }


    override fun showBookmarks() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
