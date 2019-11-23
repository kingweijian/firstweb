package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Requsetmapping {
    /**
     * 这个方法是获取(设置)请求的
     * @return
     */
    String[] value() default {};

    /**
     * 这个方法是获取(设置)参数的
     * @return
     */
    String[] parame() default {};

    /**
     * 指定头部
     * @return
     */
    String[] headrs() default {};

    /**
     *  指定contentType
     * @return
     */
    String[] consumes() default {};

    /**
     * 指定mideType (类型)
     * @return
     */
    String[] produces() default {};
}
