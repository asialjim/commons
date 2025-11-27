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

package com.asialjim.microapplet.crypt.sensitive.config;

import com.asialjim.microapplet.sensitive.encrypt.AlgorithmMode;
import com.asialjim.microapplet.sensitive.encrypt.SecretKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 密钥仓库
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
@AllArgsConstructor
public class MamsSecretKeyRepository implements SecretKeyRepository {
    private final SensitiveEncryptKeyRepository sensitiveEncryptKeyRepository;

    @Override
    public Pair pairOf(AlgorithmMode mode) {
        SensitiveEncryptKeyProperty property = this.sensitiveEncryptKeyRepository.propertyOf(mode);
        return property.pair();
    }
}