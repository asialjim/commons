/*
 *    Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.web.sensitive.annotation;

import com.asialjim.microapplet.web.sensitive.handler.SensitiveType;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注字段为敏感数据
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/17, &nbsp;&nbsp; <em>version:1.0</em>
 */
@JacksonAnnotationsInside
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensitive {

    /**
     * 敏感数据类型
     *
     * @return {@link SensitiveType }
     * @since 2025/11/18
     */
    SensitiveType value();

    /**
     * 保留前几位
     */
    int prefix() default 1;

    /**
     * 保留后几位
     */
    int suffix() default 1;

    /**
     * 是否校验敏感数据是否匹配正则 {@link #regex()}
     */
    boolean match() default true;

    /**
     * 敏感数据正则表达式
     */
    String regex() default "^[\\p{L}\\p{N}]*$";
}