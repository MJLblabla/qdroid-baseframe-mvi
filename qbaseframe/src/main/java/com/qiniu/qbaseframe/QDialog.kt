package com.qiniu.qbaseframe

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/**
 * 弹窗基类
 */
abstract class QDialog : DialogFragment() {
    @JvmField
    protected var mListener: DialogListener? = null
    private var mCancelable = true
    private var mGravityEnum = Gravity.CENTER
    private var mDimAmount = -1.0f

    /**
     * 点击事件
     *
     * @param defaultListener
     * @return
     */
    fun applyListener(defaultListener: DialogListener): QDialog {
        mListener = defaultListener
        return this
    }

    /**
     * 能否取消
     *
     * @param cancelable
     * @return
     */
    fun applyCancelable(cancelable: Boolean): QDialog {
        this.mCancelable = cancelable
        return this
    }

    /**
     * 位置
     *
     * @param gravity
     * @return
     */
    fun applyGravityStyle(gravity: Int): QDialog {
        mGravityEnum = gravity
        return this
    }

    /**
     * 北京透明度
     *
     * @param dimAmount
     * @return
     */
    fun applyDimAmount(dimAmount: Float): QDialog {
        mDimAmount = dimAmount
        return this
    }

    override fun onStart() {
        super.onStart()
        val dialog = this.dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(true)
            val window = dialog.window
            if (window != null) {
                val attributes = window.attributes
                attributes.gravity = mGravityEnum
                if (mDimAmount >= 0) {
                    attributes.dimAmount = mDimAmount
                }
                window.attributes = attributes
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.dialogFullScreen)
    }

    override fun onResume() {
        super.onResume()
        val dialog = this.dialog
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(this.mCancelable)
            if (!this.mCancelable) {
                dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog1: DialogInterface?, keyCode: Int, event: KeyEvent? -> keyCode == KeyEvent.KEYCODE_BACK })
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(requestLayoutID(), container, false)
    }

    protected abstract fun requestLayoutID(): Int
    open class DialogListener {
        open fun onDialogPositiveClick(dialog: DialogFragment) {}
        open fun onDialogNegativeClick(dialog: DialogFragment) {}
        open fun onDismiss(dialog: DialogFragment) {}
    }
}