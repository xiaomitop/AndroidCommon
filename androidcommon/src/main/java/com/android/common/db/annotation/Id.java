package com.android.common.db.annotation;

import com.android.common.db.helper.BaseColumns;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 名称：Id.java
 * 描述：主键
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface Id {
    int type() default BaseColumns.TYPE_INCREMENT;
}
