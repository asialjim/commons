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

import com.asialjim.microapplet.web.sensitive.handler.EMailSensitiveHandler;
import org.junit.Test;

import javax.crypto.SecretKey;

/**
 * 重构后的双算法加密方案使用示例
 */
public class EncryptionTest {

    @Test
    public void test() throws Exception {
        // 初始化密钥
        SecretKey modernKey = KeyManager.getOrGenerateKey(KeyManager.KEY_TYPE_MODERN);
        SecretKey gmEncryptionKey = KeyManager.getOrGenerateKey(KeyManager.KEY_TYPE_GM_ENCRYPTION);
        SecretKey gmMacKey = KeyManager.getOrGenerateKey(KeyManager.KEY_TYPE_GM_MAC);

        // 测试数据
        String sensitiveData = "110101199001011234@ggg.com";

        System.out.println("原始数据: " + sensitiveData);
        System.out.println("=====================================");

        // 使用现代算法
        testModernAlgorithm(sensitiveData, modernKey);
        System.out.println("=====================================");

        // 使用国密算法
        testGMAlgorithm(sensitiveData, gmEncryptionKey, gmMacKey);
        System.out.println("=====================================");
    }

    private static void testModernAlgorithm(String sensitiveData, SecretKey modernKey) throws Exception {
        System.out.println("=== 测试现代算法 ===");

        EncryptionContext context = new EncryptionContext(AlgorithmMode.MODERN);

        // 加密
        EncryptionResult result = context.encrypt(sensitiveData, modernKey, null);

        String mask = new EMailSensitiveHandler().mask(sensitiveData);
        String formatted = result.withMask(mask);
        System.out.println("加密结果: " + formatted);

        // 解密
        String decrypted = context.decrypt(result, modernKey, null);
        System.out.println("解密结果: " + decrypted);
        System.out.println("验证结果: " + sensitiveData.equals(decrypted));
    }

    private static void testGMAlgorithm(String sensitiveData, SecretKey gmEncryptionKey, SecretKey gmMacKey) throws Exception {
        System.out.println("=== 测试国密算法 ===");

        EncryptionContext context = new EncryptionContext(AlgorithmMode.GM);

        // 加密
        EncryptionResult result = context.encrypt(sensitiveData, gmEncryptionKey, gmMacKey);
        String mask = new EMailSensitiveHandler().mask(sensitiveData);
        String formatted = result.withMask(mask);
        System.out.println("加密结果: " + formatted);

        // 解密
        String decrypted = context.decrypt(result, gmEncryptionKey, gmMacKey);
        System.out.println("解密结果: " + decrypted);
        System.out.println("验证结果: " + sensitiveData.equals(decrypted));
    }
}