package r.rural.awaassakhi.utils

import android.view.View

interface MyRecyclerListener {

    fun onRecyclerEvent(position: Int, taskIdentifier: String, views: Array<View?>?) {}

    fun onRecyclerEvent(position: Int, taskIdentifier: String) {}

    fun onRecyclerEvent(position: Int, taskIdentifier: String, boolean: Boolean) {}

    fun onRecyclerEvent(position: Int) {}
}