package com.qiniu.qbaseframe.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.chad.library.adapter.base.module.LoadMoreModule

interface QLoadMoreModule : LoadMoreModule {
    override fun addLoadMoreModule(baseQuickAdapter: BaseQuickAdapter<*, *>): BaseLoadMoreModule {
        return QBaseLoadMoreModule(baseQuickAdapter)
    }
}

class QBaseLoadMoreModule(baseQuickAdapter: BaseQuickAdapter<*, *>) :
    BaseLoadMoreModule(baseQuickAdapter) {

    private class OnLoadMoreListenerWrap(val listener: OnLoadMoreListener) : OnLoadMoreListener {
        var ignoreOne = false
        override fun onLoadMore() {
            if (!ignoreOne) {
                listener.onLoadMore()
            } else {
                ignoreOne = false
            }
        }
    }

    private var mListener: OnLoadMoreListenerWrap? = null

    override fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        if (listener != null) {
            mListener = OnLoadMoreListenerWrap(listener)
            super.setOnLoadMoreListener(mListener)
        } else {
            mListener = null
            super.setOnLoadMoreListener(listener)
        }
    }

    fun reLoading() {
        if (mListener != null) {
            mListener?.ignoreOne = true
        }
        loadMoreToLoading()
    }

}