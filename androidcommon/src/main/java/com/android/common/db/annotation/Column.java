package com.android.common.db.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 名称：Column.java
 * 描述：表示列
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface Column {
	
	/**
	 * 列名.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * 列类型.
	 *
	 * @return the string
	 */
	String type() default "";

	/**
	 * 长度.
	 *
	 * @return the int
	 */
	int length() default 0;

	/**
	 * 外键.
	 *
	 * @return the boolean
	 */
	boolean foreign() default false;

}
