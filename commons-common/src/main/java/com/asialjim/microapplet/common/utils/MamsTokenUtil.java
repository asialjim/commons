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

package com.asialjim.microapplet.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * mams 令牌 生成工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class MamsTokenUtil {

    private static final SecureRandom RND = new SecureRandom();
    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    private static final long BEGIN = 1726000000L; // 秒级基准

    /**
     */
    private MamsTokenUtil() {
    }


    /**
     * 秘密
     *
     * @return {@link String}
     */
    public static String secret() {
        String property = System.getProperty("com.asialjim.microapplet.token.secret");
        if (StringUtils.isNotBlank(property)) {
            int length = StringUtils.length(property);
            if (length == 16)
                return property;
            else if (length > 16)
                return property.substring(0, 16);
            else {
                int nextLength = 16 - length;
                return property + "0".repeat(Math.max(0, nextLength));
            }
        }

        return "SixteenBytesKey!";
    }

    /**
     * 生成 36 字符 state
     */
    public static String create() {
        /* 1. 9 字节随机 -> 12 字符 */
        byte[] rand = new byte[9];
        RND.nextBytes(rand);
        String r = B64.encodeToString(rand);

        /* 2. 相对秒 4 字节 -> 6 字符 */
        int diff = (int) ((System.currentTimeMillis() / 1000L - BEGIN) & 0xFFFFFFFFL);
        String t = B64.encodeToString(new byte[]{
                (byte) (diff >>> 24), (byte) (diff >>> 16),
                (byte) (diff >>> 8), (byte) diff});

        /* 3. MAC 12 字节 -> 16 字符（不截断） */
        String payload = r + "." + t;
        byte[] mac12 = new byte[12];
        System.arraycopy(hmac(payload), 0, mac12, 0, 12);
        String m = B64.encodeToString(mac12);   // 16 字符

        return payload + "." + m;               // 12+6+16+2 = 36
    }

    /**
     * 校验 state
     */
    public static boolean verify(String state) {
        if (state == null || StringUtils.length(state) != 36) return false;
        String[] p = state.split("\\.", -1);
        if (p.length != 3) return false;
        byte[] mac12 = new byte[12];
        System.arraycopy(hmac(p[0] + "." + p[1]), 0, mac12, 0, 12);
        String expect = B64.encodeToString(mac12);
        return expect.equals(p[2]);
    }

    /**
     * hmac
     *
     * @param data 数据
     * @return {@link byte[]}
     */
    private static byte[] hmac(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret().getBytes(), "HmacSHA256"));
            return mac.doFinal(data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}