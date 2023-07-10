package com.qiniu.qbaseframe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.qiniu.qbaseframe.vm.LoadingObserverView
import com.qiniu.qbaseframe.vm.ToastObserverView
import java.lang.reflect.ParameterizedType
import java.util.Objects

abstract class QBindingActivity<T : ViewBinding> : AppCompatActivity(), LoadingObserverView,
    ToastObserverView {

    protected lateinit var binding: T
    private fun createContainerView(): View {
        var sup = this.javaClass.genericSuperclass
        if (sup !is ParameterizedType) {
            sup = Objects.requireNonNull(this.javaClass.superclass).genericSuperclass
        }
        assert(sup != null)
        binding = com.qiniu.qbaseframe.ViewBindingExt.create(sup as ParameterizedType, null, this, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createContainerView())
        initView()
    }

    protected abstract fun initView()

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}