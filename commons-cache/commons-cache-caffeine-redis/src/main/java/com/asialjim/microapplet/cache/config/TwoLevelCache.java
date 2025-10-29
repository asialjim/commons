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

package com.asialjim.microapplet.cache.config;

import org.springframework.cache.Cache;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 二级缓存
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/10/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class TwoLevelCache implements Cache {
    private final String name;
    private final Cache caffeineCache; // 一级缓存
    private final Cache redisCache; // 二级缓存

    public TwoLevelCache(String name, Cache caffeineCache, Cache redisCache) {
        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisCache = redisCache;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        // 通常返回底层缓存，如Redis
        return redisCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        // 1. 先查一级缓存
        ValueWrapper value = caffeineCache.get(key);
        if (value != null) {
            return value;
        }
        // 2. 一级缓存未命中，查二级缓存
        value = redisCache.get(key);
        if (value != null) {
            // 3. 将二级缓存中取到的数据回填到一级缓存
            caffeineCache.put(key, value.get());
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        // 实现逻辑类似，先查一级缓存，再查二级缓存
        T value = caffeineCache.get(key, type);
        if (value != null) {
            return value;
        }
        value = redisCache.get(key, type);
        if (value != null) {
            caffeineCache.put(key, value);
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // 二级缓存
        Callable<T> redisCallable = () -> {
            T t = redisCache.get(key, valueLoader);
            if (Objects.nonNull(t))
                caffeineCache.put(key, t);
            return t;
        };

        return caffeineCache.get(key, redisCallable);
    }

    @Override
    public void put(Object key, Object value) {
        // 写入时，同时写入两级缓存
        redisCache.put(key, value);
        caffeineCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        // 失效时，同时失效两级缓存
        redisCache.evict(key);
        caffeineCache.evict(key);
    }

    @Override
    public void clear() {
        redisCache.clear();
        caffeineCache.clear();
    }
}