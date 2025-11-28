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

package com.asialjim.microapplet.web.mvc.config;

import com.asialjim.microapplet.common.utils.JacksonUtil;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 基于 jackson 的 HTTP 消息配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Configuration
public class JacksonHttpMessageConfiguration {

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {

        return mapperData();
        //return mapperDefault();
    }

    private MappingJackson2HttpMessageConverter mapperData() {
        Map<Class<?>, JsonSerializer<?>> serializers = JacksonUtil.serializers();
        Map<Class<?>, JsonDeserializer<?>> deserializers = JacksonUtil.deserializers();

        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .serializersByType(serializers)
                .deserializersByType(deserializers)
                .build();
        return new MappingJackson2HttpMessageConverter(mapper);
    }

    private MappingJackson2HttpMessageConverter mapperDefault() {
        ObjectMapper build = Jackson2ObjectMapperBuilder.json().build();
        ObjectMapper mapper = JacksonUtil.init(build);
        Assert.notNull(mapper, "mapper cannot be null");
        return new MappingJackson2HttpMessageConverter(mapper);
    }
}