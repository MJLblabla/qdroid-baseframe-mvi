package com.qiniu.qbaseframe.adapter

import android.view.View

interface IEmptyView {
    val contentView: View?
    fun setStatus(type: Int)

    companion object {
        /**
         * 隐藏
         */
        const val HIDE_LAYOUT = 3

        /**
         * 网络异常
         */
        const val NETWORK_ERROR = 1

        /**
         * 服务器数据空
         */
        const val NODATA = 2
        const val START_REFREASH_WHEN_EMPTY = -1 //数据为空时候刷新
    }
}