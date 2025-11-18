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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**
 * 密钥管理器
 */
public class KeyManager {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Map<String, SecretKey> keyCache = new HashMap<>();

    // 密钥类型常量
    public static final String KEY_TYPE_MODERN = "MODERN_ENCRYPTION";
    public static final String KEY_TYPE_GM_ENCRYPTION = "GM_ENCRYPTION";
    public static final String KEY_TYPE_GM_MAC = "GM_MAC";

    /**
     * 生成现代算法加密密钥
     */
    public static SecretKey generateModernEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("ChaCha20");
        keyGenerator.init(256, secureRandom);
        return keyGenerator.generateKey();
    }

    /**
     * 生成国密算法加密密钥
     */
    public static SecretKey generateGMEncryptionKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        // 注册Bouncy Castle Provider
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("SM4", "BC");
        keyGenerator.init(128, secureRandom);
        return keyGenerator.generateKey();
    }

    /**
     * 生成国密算法MAC密钥
     */
    public static SecretKey generateGMMacKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSM3");
        keyGenerator.init(256, secureRandom);
        return keyGenerator.generateKey();
    }

    /**
     * 获取或生成密钥
     */
    public static SecretKey getOrGenerateKey(String keyType) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (!keyCache.containsKey(keyType)) {
            SecretKey key = switch (keyType) {
                case KEY_TYPE_MODERN -> generateModernEncryptionKey();
                case KEY_TYPE_GM_ENCRYPTION -> generateGMEncryptionKey();
                case KEY_TYPE_GM_MAC -> generateGMMacKey();
                default -> throw new IllegalArgumentException("不支持的密钥类型: " + keyType);
            };
            keyCache.put(keyType, key);
        }
        return keyCache.get(keyType);
    }
}