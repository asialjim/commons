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

import com.asialjim.microapplet.web.sensitive.handler.SensitiveHandler;
import com.asialjim.microapplet.web.sensitive.handler.SensitiveType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 敏感数据序列化工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/17, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private final Sensitive sensitive;

    public SensitiveSerializer() {
        this(null);
    }

    public SensitiveSerializer(Sensitive sensitive) {
        this.sensitive = sensitive;
    }


    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SensitiveType type = sensitive.value();
        String mask = SensitiveHandler.mask(type, s);

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (Objects.isNull(beanProperty))
            return this;

        Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
        if (Objects.isNull(annotation))
            return this;
        return new SensitiveSerializer(annotation);
    }
}