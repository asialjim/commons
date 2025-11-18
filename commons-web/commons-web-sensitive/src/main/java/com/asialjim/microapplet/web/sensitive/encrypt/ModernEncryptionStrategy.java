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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * 现代算法策略实现 - ChaCha20-Poly1305 / AES-GCM
 */
public class ModernEncryptionStrategy implements EncryptionStrategy {
    private static final String MODERN_ENCRYPTION_ALGORITHM = "ChaCha20-Poly1305";
    private static final int MODERN_NONCE_LENGTH = 12;
    private static final int MODERN_TAG_LENGTH = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey))
            throw new IllegalArgumentException("现代算法需要加密密钥和MAC密钥");

        // 生成随机数
        byte[] nonce = new byte[MODERN_NONCE_LENGTH];
        secureRandom.nextBytes(nonce);

        // 初始化Cipher
        Cipher cipher = Cipher.getInstance(MODERN_ENCRYPTION_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(MODERN_TAG_LENGTH, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, parameterSpec);

        // 执行加密和认证
        byte[] ciphertextWithTag = cipher.doFinal(sensitiveData.getBytes(StandardCharsets.UTF_8));

        // 分离密文和认证标签
        int ciphertextLength = ciphertextWithTag.length - MODERN_TAG_LENGTH / 8;
        byte[] encrypted = Arrays.copyOf(ciphertextWithTag, ciphertextLength);
        byte[] mac = Arrays.copyOfRange(ciphertextWithTag, ciphertextLength, ciphertextWithTag.length);

        return new EncryptionResult(AlgorithmMode.MODERN, nonce, encrypted, mac);
    }

    @Override
    public String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey))
            throw new IllegalArgumentException("现代算法需要加密密钥和MAC密钥");

        // 重新组合密文和认证标签
        byte[] ciphertextWithTag = new byte[encryptedData.getEncrypt().length + encryptedData.getMac().length];
        System.arraycopy(encryptedData.getEncrypt(), 0, ciphertextWithTag, 0, encryptedData.getEncrypt().length);
        System.arraycopy(encryptedData.getMac(), 0, ciphertextWithTag, encryptedData.getEncrypt().length, encryptedData.getMac().length);

        // 初始化解密Cipher
        Cipher cipher = Cipher.getInstance(MODERN_ENCRYPTION_ALGORITHM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(MODERN_TAG_LENGTH, encryptedData.getNonce());
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, parameterSpec);

        // 执行解密和验证
        byte[] decrypted = cipher.doFinal(ciphertextWithTag);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    @Override
    public AlgorithmMode getAlgorithmMode() {
        return AlgorithmMode.MODERN;
    }

    @Override
    public boolean supports(String formattedData) {
        try {
            String[] parts = formattedData.split("\\|");
            return parts.length >= 2 && AlgorithmMode.MODERN.getCode().equals(parts[1]);
        } catch (Exception e) {
            return false;
        }
    }
}