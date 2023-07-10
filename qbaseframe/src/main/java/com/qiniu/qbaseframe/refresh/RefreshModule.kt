package  com.qiniu.qbaseframe.refresh

import android.animation.ObjectAnimator
import android.content.Context
import android.view.View


abstract class RefreshModule {
    interface Listener {
        fun onStartRefresh()
    }

    var isReFreshEnable = true
    var isRefreshing: Boolean = false
        internal set
    var listener: Listener? = null
    abstract fun startRefresh()
    abstract fun setRefreshing()
    abstract fun finishRefresh()
}

abstract class BaseRefreshModule(val context: Context) : RefreshModule() {

    internal var recoverAnimator: ObjectAnimator? = null
    internal abstract fun getFreshTopHeight(): Int
    internal abstract fun getFreshHeight(): Int
    internal abstract fun getAttachView(): View
    internal abstract fun isFloat(): Boolean

    internal abstract fun onPointMove(totalY: Float, dy: Float): Float
    internal abstract fun onPointUp(toStartRefresh: Boolean)
    internal abstract fun onFinishRefresh()

    internal lateinit var parent: QRefreshLayout

    override fun startRefresh() {
        if (!isReFreshEnable) {
            return
        }
        setRefreshing()
        listener?.onStartRefresh()
    }

    override fun setRefreshing() {
        if (!isReFreshEnable) {
            return
        }
        if (isRefreshing || parent.loadMoreModule?.isLoading == true) {
            return
        }
        isRefreshing = true
        recoverAnimator?.cancel()
        if (isFloat()) {
            recoverAnimator =
                ObjectAnimator.ofFloat(
                    getAttachView(),
                    "translationY",
                    getAttachView().translationY,
                    getFreshTopHeight().toFloat()
                )
            recoverAnimator?.duration = 300
            recoverAnimator?.start()

        } else {
            parent.scrollTo(0, (getFreshTopHeight()))
        }
        onPointUp(true)
    }

    override fun finishRefresh() {
        if (!isRefreshing) {
            return
        }
        isRefreshing = false
        onFinishRefresh()
        if (isFloat()) {
            recoverAnimator?.cancel()
            recoverAnimator =
                ObjectAnimator.ofFloat(
                    getAttachView(),
                    "translationY",
                    getAttachView().translationY,
                    -getFreshHeight().toFloat()
                )
            recoverAnimator?.duration = 300
            recoverAnimator?.start()
        }
        // if (!isEmpty) {
        parent.mLoadMoreModule?.checkHideNoMore()
        // }
        if (parent.scrollY != 0) {
            parent.scrollTo(0, 0)
        }
    }
}
