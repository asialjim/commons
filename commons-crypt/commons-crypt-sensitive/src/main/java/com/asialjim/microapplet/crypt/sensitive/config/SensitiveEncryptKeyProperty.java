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
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serial;
import java.io.Serializable;
import java.util.Base64;

/**
 * 敏感数据加解密密钥配置信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Accessors(chain = true)
public class SensitiveEncryptKeyProperty implements Serializable {
    @Serial
    private static final long serialVersionUID = 641708416790905106L;

    /**
     * 加密模式
     */
    private String mode;

    /**
     * 加密密钥
     */
    private String encKey;

    /**
     * 签名密钥
     */
    private String macKey;


    public AlgorithmMode mode() {
        return AlgorithmMode.fromCode(this.mode);
    }

    public SecretKeyRepository.Pair pair() {
        AlgorithmMode mode = mode();
        SecretKeyRepository.Pair pair = new SecretKeyRepository.Pair();

        if (StringUtils.isNotBlank(this.encKey)) {
            byte[] encKeyBuf = Base64.getDecoder().decode(this.encKey);
            SecretKeySpec enc = new SecretKeySpec(encKeyBuf, mode.getEncAlgorithm());
            pair.setEncKey(enc);
        }

        if (StringUtils.isNotBlank(this.macKey)) {
            byte[] macKeyBuf = Base64.getDecoder().decode(this.macKey);
            SecretKeySpec mac = new SecretKeySpec(macKeyBuf, mode.getMacAlgorithm());

            pair.setEncKey(mac);
        }

        return pair;
    }
}