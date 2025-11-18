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

package com.asialjim.microapplet.web.sensitive.encrypt;

import javax.crypto.SecretKey;

/**
 * 加解密策略
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/11/18, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface EncryptionStrategy {
    /**
     * 加密敏感数据
     */
    EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception;

    /**
     * 解密数据
     */
    String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception;

    /**
     * 获取算法模式
     */
    AlgorithmMode getAlgorithmMode();

    /**
     * 验证数据格式是否支持
     */
    boolean supports(String formattedData);
}