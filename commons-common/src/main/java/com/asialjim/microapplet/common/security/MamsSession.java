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

package com.asialjim.microapplet.common.security;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * MAMS 会话信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/19, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Accessors(chain = true)
public final class MamsSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 709846307867973606L;

    /**
     * 会话编号
     */
    private String id;

    /**
     * 会话令牌
     */
    private String token;

    /**
     * 登录所属主应用编号
     */
    private String appid;

    /**
     * 登录主用户编号
     */
    private String userid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 角色位图
     */
    private long roleBit = 0;
    private String rolBitStr;

    public String getRoleBitStr(){
        return String.valueOf(this.roleBit);
    }

    /**
     * 添加角色
     *
     * @param roleBit 用户角色位图
     */
    public void addRole(long roleBit) {
        if ((this.roleBit & roleBit) == roleBit)
            return;
        this.roleBit |= roleBit; // 使用按位或添加角色
    }

    /**
     * 登录渠道
     */
    private String chl;

    /**
     * 登录渠道应用
     */
    private String chlAppid;

    /**
     * 登录渠道应用类型
     */
    private String chlAppType;

    /**
     * 登录渠道用户编号
     */
    private String chlUserid;
    private String chlUnionid;
    private String chlUserCode;
    private String chlUserToken;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    private LocalDateTime expireAt;

    /**
     * 到期后
     *
     * @param duration 持续时间
     */
    public MamsSession expireAfter(Duration duration) {
        this.expireAt = LocalDateTime.now().plusMinutes(duration.toMinutes());
        return this;
    }

    public boolean isExpired() {
        LocalDateTime expireAt = getExpireAt();
        if (Objects.isNull(expireAt))
            return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expireAt);
    }
}