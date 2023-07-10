package com.qiniu.qbaseframe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding

import com.qiniu.qbaseframe.vm.LoadingObserverView
import com.qiniu.qbaseframe.vm.ToastObserverView
import java.lang.reflect.ParameterizedType
import java.util.Objects

abstract class QBindDialog<T : ViewBinding> : com.qiniu.qbaseframe.QDialog(), LoadingObserverView, ToastObserverView {

    protected lateinit var binding: T
    private fun createContainerView(container: ViewGroup?): View {
        var sup = this.javaClass.genericSuperclass
        if (sup !is ParameterizedType) {
            sup = Objects.requireNonNull(this.javaClass.superclass).genericSuperclass
        }
        assert(sup != null)
        binding =
            com.qiniu.qbaseframe.ViewBindingExt.create(
                sup as ParameterizedType,
                container,
                requireContext(),
                false
            )
        return binding.root
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createContainerView(container)
    }

    override fun requestLayoutID(): Int {
        return -1
    }
//
//    override fun showLoading(toShow: Boolean) {
//        if (toShow) {
//            LoadingDialog.showLoading(childFragmentManager)
//        } else {
//            LoadingDialog.cancelLoadingDialog()
//        }
//    }

    override fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}