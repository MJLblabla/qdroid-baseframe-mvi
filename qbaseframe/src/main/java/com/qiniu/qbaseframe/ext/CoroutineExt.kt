package com.qiniu.qbaseframe.ext

import kotlinx.coroutines.*
import java.lang.Exception


class CoroutineScopeWrap {
    var work: (suspend CoroutineScope.() -> Unit) = {}
    var error: (e: Throwable) -> Unit = {}
    var complete: () -> Unit = {}

    fun doWork(call: suspend CoroutineScope.() -> Unit) {
        this.work = call
    }

    fun catchError(error: (e: Throwable) -> Unit) {
        this.error = error
    }

    fun onFinally(call: () -> Unit) {
        this.complete = call
    }
}

fun backGround(
    dispatcher: MainCoroutineDispatcher = Dispatchers.Main,
    c: CoroutineScopeWrap.() -> Unit
) {
    GlobalScope
        .launch(dispatcher) {
            val block = CoroutineScopeWrap()
            c.invoke(block)
            try {
                block.work.invoke(this)
            } catch (e: Exception) {
                e.printStackTrace()
                block.error.invoke(e)
            } finally {
                block.complete.invoke()
            }
        }
}

fun CoroutineScope.backGround(
    dispatcher: MainCoroutineDispatcher = Dispatchers.Main,
    c: CoroutineScopeWrap.() -> Unit
) {
    this.launch(dispatcher) {
        val block = CoroutineScopeWrap()
        c.invoke(block)
        try {
            block.work.invoke(this)
        } catch (e: Exception) {
            block.error.invoke(e)
        } finally {
            block.complete.invoke()
        }
    }
}

fun CoroutineScope.doWork(block: suspend CoroutineScope.() -> Unit) {
    this.launch {
        try {
            block.invoke(this)
        } catch (e: Exception) {
        }
    }
}