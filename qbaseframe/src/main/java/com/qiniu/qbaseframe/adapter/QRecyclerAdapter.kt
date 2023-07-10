package com.qiniu.qbaseframe.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.chad.library.adapter.base.module.BaseUpFetchModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule
import java.util.LinkedList

abstract class QRecyclerAdapter<T>() :
    BaseQuickAdapter<T, QRecyclerViewHolder>(0, LinkedList<T>()) {

    protected var iEmptyView: IEmptyView? = null
    fun attachEmptyView(emptyView: IEmptyView) {
        this.iEmptyView = emptyView
        iEmptyView!!.contentView?.let {
            setEmptyView(it)
        }
    }

    fun detachEmptyView() {
        removeEmptyView()
    }

    fun setEmptyViewStatus(int: Int) {
        iEmptyView?.setStatus(int)
    }

    fun selfGetLoadMoreModule(): BaseLoadMoreModule? {
        return if (this is LoadMoreModule) {
            loadMoreModule
        } else {
            null
        }
    }

    fun selfGetUpFetchModule(): BaseUpFetchModule? {
        return if (this is UpFetchModule) {
            upFetchModule
        } else {
            null
        }
    }
}