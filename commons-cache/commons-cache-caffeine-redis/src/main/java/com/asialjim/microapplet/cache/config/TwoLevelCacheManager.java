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

import org.springframework.lang.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Configuration("twoLevelCacheManager")
public class TwoLevelCacheManager implements CacheManager {
    private final CacheManager caffeineCacheManager; // 一级缓存管理器
    private final CacheManager redisCacheManager; // 二级缓存管理器
    private final Collection<String> cacheNames = new LinkedList<>();
    private final ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public TwoLevelCacheManager(@Qualifier("caffeineCacheManager") CacheManager caffeineCacheManager,
                                @Qualifier("redisCacheManager") CacheManager redisCacheManager) {

        this.caffeineCacheManager = caffeineCacheManager;
        this.redisCacheManager = redisCacheManager;

        // 合并两个CacheManager的缓存名称
        this.cacheNames.addAll(caffeineCacheManager.getCacheNames());
        this.cacheNames.addAll(redisCacheManager.getCacheNames());
    }

    @Override
    public Cache getCache(@SuppressWarnings("NullableProblems") String name) {
        return cacheMap.computeIfAbsent(name, cacheName -> new TwoLevelCache(cacheName, caffeineCacheManager.getCache(cacheName), redisCacheManager.getCache(cacheName)));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Collection<String> getCacheNames() {
        return cacheNames;
    }
}