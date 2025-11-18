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

package com.asialjim.microapplet.web.sensitive.handler;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * 邮箱脱敏工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/18, &nbsp;&nbsp; <em>version:1.0
 * </em>
 */
public class EMailSensitiveHandler extends SensitiveHandler {
    @Override
    public SensitiveType type() {
        return SensitiveType.EMail;
    }

    @Override
    protected Function<String, String> function() {
        return s -> {
            String[] split = StringUtils.split(s, '@');
            int length = ArrayUtils.getLength(split);
            if (length != 2)
                throw new IllegalArgumentException("非法的E-Mail");

            length = StringUtils.length(split[0]);
            if (length < 1)
                throw new IllegalArgumentException("非法的E-Mail");

            if (length == 1)
                return "*@" + split[1];

            if (length < 5)
                return split[0].substring(0, 2) + "*****@" + split[1];

            return split[0].substring(0, 4) + "*****@" + split[1];
        };
    }
}