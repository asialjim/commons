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
import com.asialjim.microapplet.sensitive.encrypt.AlgorithmModeConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 敏感数据加解密密钥配置信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Slf4j
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "sensitive.encrypt")
public class SensitiveEncryptKeyProperties implements SensitiveEncryptKeyRepository, AlgorithmModeConfig, Serializable {

    @Serial
    private static final long serialVersionUID = -9139102388056110980L;

    private String mode;
    private List<SensitiveEncryptKeyProperty> keys;

    @Override
    public SensitiveEncryptKeyProperty propertyOf(AlgorithmMode algorithmMode) {
        for (SensitiveEncryptKeyProperty key : keys) {
            String mode = key.getMode();
            if (StringUtils.equals(mode, Optional.ofNullable(algorithmMode).map(AlgorithmMode::getCode).orElse(StringUtils.EMPTY))) {
                return key;
            }
        }
        throw new IllegalArgumentException("未知的算法模式: " + algorithmMode);
    }

    @Override
    public AlgorithmMode currentMode() {
        return AlgorithmMode.fromCode(this.mode);
    }
}