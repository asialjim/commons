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

import lombok.Setter;

import javax.crypto.SecretKey;

/**
 * 加密上下文 - 策略模式上下文
 */
@Setter
public class EncryptionContext {
    private EncryptionStrategy strategy;

    public EncryptionContext() {
        // 默认使用国密方案
        this.strategy = EncryptionStrategyFactory.getStrategy(AlgorithmMode.GM);
    }

    public EncryptionContext(AlgorithmMode mode) {
        this.strategy = EncryptionStrategyFactory.getStrategy(mode);
    }

    public EncryptionContext(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }


    /**
     * 设置算法模式
     */
    public void setAlgorithmMode(AlgorithmMode mode) {
        this.strategy = EncryptionStrategyFactory.getStrategy(mode);
    }

    /**
     * 加密数据
     */
    public EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        return strategy.encrypt(sensitiveData, encryptionKey,macKey);
    }

    /**
     * 解密数据
     */
    public String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        return strategy.decrypt(encryptedData, encryptionKey,macKey);
    }

    /**
     * 解密格式化字符串数据
     */
    public String decrypt(String formattedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        EncryptionResult encryptedData = EncryptionResult.fromFormattedString(formattedData);
        // 根据数据自动选择策略
        this.strategy = EncryptionStrategyFactory.getStrategyForData(formattedData);
        return strategy.decrypt(encryptedData, encryptionKey,macKey);
    }

    /**
     * 获取当前算法模式
     */
    public AlgorithmMode getCurrentAlgorithmMode() {
        return strategy.getAlgorithmMode();
    }
}