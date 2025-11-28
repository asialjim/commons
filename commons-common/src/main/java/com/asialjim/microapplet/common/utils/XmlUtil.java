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

package com.asialjim.microapplet.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * XML 工具，基于 Jackson
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/7, &nbsp;&nbsp; <em>version:1.0</em>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class XmlUtil extends JacksonUtil {
    public static final JacksonUtil instance = new XmlUtil();
    private static final ObjectMapper mapper = init(new XmlMapper());

    @Override
    public ObjectMapper objectMapper() {
        return mapper;
    }
}