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

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Objects;

/**
 * 国密算法策略实现 - SM4加密 + SM3消息认证
 */
public class GMEncryptionStrategy implements EncryptionStrategy {

    //private static final String GM_ENCRYPTION_ALGORITHM = "SM4";
    private static final String GM_CIPHER_TRANSFORMATION = "SM4/CBC/PKCS5Padding";
    private static final String GM_MAC_ALGORITHM = "HmacSM3";
    private static final int GM_IV_LENGTH = 16;

    private final SecureRandom secureRandom = new SecureRandom();
    static {

    }

    @Override
    public EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey) || Objects.isNull(macKey)) {
            throw new IllegalArgumentException("国密算法需要加密密钥和MAC密钥");
        }

        // 生成IV
        byte[] iv = new byte[GM_IV_LENGTH];
        secureRandom.nextBytes(iv);

        // SM4加密
        Cipher cipher = Cipher.getInstance(GM_CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(sensitiveData.getBytes(StandardCharsets.UTF_8));

        // 使用HMAC-SM3计算MAC
        Mac mac = Mac.getInstance(GM_MAC_ALGORITHM);
        mac.init(macKey);
        mac.update(encrypted);
        byte[] macBytes = mac.doFinal();

        return new EncryptionResult(AlgorithmMode.GM, iv, encrypted, macBytes);
    }

    @Override
    public String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey) || Objects.isNull(macKey)) {
            throw new IllegalArgumentException("国密算法需要加密密钥和MAC密钥");
        }

        // 验证MAC
        Mac mac = Mac.getInstance(GM_MAC_ALGORITHM);
        mac.init(macKey);
        mac.update(encryptedData.getEncrypt());
        byte[] calculatedMac = mac.doFinal();

        if (!MessageDigest.isEqual(calculatedMac, encryptedData.getMac())) {
            throw new SecurityException("MAC验证失败，数据可能被篡改");
        }

        // SM4解密
        Cipher cipher = Cipher.getInstance(GM_CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, new IvParameterSpec(encryptedData.getNonce()));
        byte[] decrypted = cipher.doFinal(encryptedData.getEncrypt());
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    @Override
    public AlgorithmMode getAlgorithmMode() {
        return AlgorithmMode.GM;
    }

    @Override
    public boolean supports(String formattedData) {
        try {
            String[] parts = formattedData.split("\\|");
            return parts.length >= 2 && AlgorithmMode.GM.getCode().equals(parts[1]);
        } catch (Exception e) {
            return false;
        }
    }
}