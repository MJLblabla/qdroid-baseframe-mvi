package com.qiniu.qbaseframe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public class ViewBindingExt {

    public static <T> T create(ParameterizedType type, ViewGroup viewGroup, Context context, Boolean attach) {
        T bind = null;
        Class<?> cls = (Class<?>) type.getActualTypeArguments()[0];
        try {
            Method mInflate = cls.getDeclaredMethod(
                    "inflate",
                    LayoutInflater.class,
                    ViewGroup.class,
                    boolean.class
            );
            bind = (T) mInflate.invoke(null, LayoutInflater.from(context), viewGroup, attach);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return bind;
    }
}
