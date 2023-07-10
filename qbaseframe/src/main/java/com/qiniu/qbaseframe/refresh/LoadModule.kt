package  com.qiniu.qbaseframe.refresh

import android.content.Context
import android.view.View

abstract class LoadModule(val context: Context) {

    interface Listener {
        fun onStartLoadMore()
    }

    var listener: Listener? = null
    var isLoading: Boolean = false
        internal set
    var isLoadMoreEnable = true
    var goneIfNoData: Boolean = true

    abstract fun setLoading()
    abstract fun finishLoadMore(
        noMore: Boolean
    )
}

abstract class BaseLoadModule(context: Context) : LoadModule(context) {
    lateinit var parent: QRefreshLayout
    var isShowLoadMore = false
        internal set
    var isShowLoading = false
        internal set
    var scrollToNextPageVisibility: Boolean = true
        internal set

    abstract fun checkHideNoMore()
    abstract fun getFreshHeight(): Int
    abstract fun getAttachView(): View
    abstract fun onPointMove(totalY: Float, dy: Float): Float

    open fun onPointUp(toStartLoad: Boolean) {
        isShowLoading = toStartLoad
    }

    protected open fun onFinishLoad(showNoMore: Boolean) {
        isShowLoading = false
        isShowLoadMore = showNoMore
    }

    override fun finishLoadMore(
        noMore: Boolean
    ) {
        if (!isLoading) {
            return
        }
        isLoading = false
        if (noMore) {
            //没有更多了
            if (goneIfNoData) {
                //不可见
                onFinishLoad(false)
                parent.scrollTo(0, 0)
            } else {
                onFinishLoad(true)
            }
        } else {
            //还有更多
            onFinishLoad(false)
            if (scrollToNextPageVisibility) {
                parent.mScrollView.scrollBy(0, parent.scrollY)
            }
            parent.scrollTo(0, 0)
        }
    }

    override fun setLoading() {
        if (!isLoadMoreEnable) {
            return
        }
        isLoading = true
        listener?.onStartLoadMore()
        onPointUp(true)
        parent.scrollTo(0, getFreshHeight())
    }
}