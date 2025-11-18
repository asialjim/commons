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

import java.util.HashMap;
import java.util.Map;

/**
 * 加密策略工厂 - 工厂模式
 */
public class EncryptionStrategyFactory {
    private static final Map<AlgorithmMode, EncryptionStrategy> strategies = new HashMap<>();

    static {
        // 注册所有策略
        strategies.put(AlgorithmMode.GM, new GMEncryptionStrategy());
        strategies.put(AlgorithmMode.MODERN, new ModernEncryptionStrategy());
    }

    /**
     * 根据算法模式获取策略
     */
    public static EncryptionStrategy getStrategy(AlgorithmMode mode) {
        EncryptionStrategy strategy = strategies.get(mode);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的算法模式: " + mode);
        }
        return strategy;
    }

    /**
     * 根据格式化数据自动选择策略
     */
    public static EncryptionStrategy getStrategyForData(String formattedData) {
        for (EncryptionStrategy strategy : strategies.values()) {
            if (strategy.supports(formattedData)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("没有找到支持该数据格式的加密策略");
    }

    /**
     * 注册新策略
     */
    public static void registerStrategy(AlgorithmMode mode, EncryptionStrategy strategy) {
        strategies.put(mode, strategy);
    }

    /**
     * 获取所有支持的算法模式
     */
    public static AlgorithmMode[] getSupportedModes() {
        return strategies.keySet().toArray(new AlgorithmMode[0]);
    }
}