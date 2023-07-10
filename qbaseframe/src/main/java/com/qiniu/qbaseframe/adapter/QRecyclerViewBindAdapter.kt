package  com.qiniu.qbaseframe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

abstract class QRecyclerViewBindAdapter<T, R : ViewBinding>() : QRecyclerAdapter<T>() {

    private fun <T : ViewBinding> create(
        sup: ParameterizedType,
        viewGroup: ViewGroup?,
        context: Context,
        attach: Boolean
    ): T {
        var binding: T? = null
        val cls = (sup as ParameterizedType).actualTypeArguments[1] as Class<*>
        try {
            val mInflate = cls.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            binding = mInflate.invoke(null, LayoutInflater.from(context), viewGroup, attach) as T
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return binding!!
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): QRecyclerViewHolder {
        val sup = javaClass.genericSuperclass
        val binding = create<R>(sup as ParameterizedType, parent, context, false)
        return QRecyclerViewBindHolder<R>(binding, binding.root)
    }

    final override fun convert(holder: QRecyclerViewHolder, item: T) {
        convertViewBindHolder(holder as QRecyclerViewBindHolder<R>, item)
    }

    abstract fun convertViewBindHolder(helper: QRecyclerViewBindHolder<R>, item: T)
}