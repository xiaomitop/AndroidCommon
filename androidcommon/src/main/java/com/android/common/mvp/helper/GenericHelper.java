package com.android.common.mvp.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericHelper {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getViewClass(Class<?> klass) {
        Type type = klass.getGenericSuperclass();
        if(type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if(types == null || types.length == 0) return null;
        return (Class<T>) types[0];
    }
}
