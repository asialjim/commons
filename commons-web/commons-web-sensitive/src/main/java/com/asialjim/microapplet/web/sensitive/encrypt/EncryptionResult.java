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

import com.asialjim.microapplet.web.sensitive.handler.SensitiveHandler;
import lombok.Getter;

import java.util.Base64;

@SuppressWarnings("LombokGetterMayBeUsed")
public record EncryptionResult(
        @Getter AlgorithmMode algorithmMode,
        @Getter byte[] nonce,
        @Getter byte[] encrypt,
        @Getter byte[] mac) {

    public String toFormattedString() {
        // 格式化为字符串: _mask|algorithm_flag|nonce_hex|encrypt_hex|mac_hex|mask
        String algorithmFlag = algorithmMode.getCode();
        String encryptHex = bytesToBase64Url(encrypt);
        String macHex = bytesToBase64Url(mac);
        String nonceHex = bytesToBase64Url(nonce);

        return String.format("_mask|%s|%s|%s|%s|", algorithmFlag, nonceHex, encryptHex, macHex);
    }

    public String withMask(String mask){
        String formattedString = toFormattedString();
        return formattedString + mask;
    }

    public static EncryptionResult fromFormattedString(String formattedString) {
        String[] parts = formattedString.split("\\|");
        if (parts.length != 6 || !"_mask".equals(parts[0]))
            throw new IllegalArgumentException("无效的数据格式");

        AlgorithmMode mode = AlgorithmMode.fromCode(parts[1]);
        byte[] nonce = base64UrlToBytes(parts[2]);
        byte[] encrypt = base64UrlToBytes(parts[3]);
        byte[] mac = base64UrlToBytes(parts[4]);

        return new EncryptionResult(mode, nonce, encrypt, mac);
    }


    // 工具方法
    private static String bytesToBase64Url(byte[] bytes) {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    private static byte[] base64UrlToBytes(String hexString) {
        return Base64.getUrlDecoder().decode(hexString);
    }

    @Override
    public String toString() {
        return "EncryptionResult => " + toFormattedString();
    }
}