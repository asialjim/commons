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

package com.asialjim.microapplet.common.event;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 事件工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Setter
@Component
public class EventUtil implements ApplicationContextAware, CommandLineRunner {
    private static final Map<Type, Set<Listener<?>>> listenerHub = new HashMap<>();
    private ApplicationContext applicationContext;

    private static void add(Type type, Listener<?> listener) {
        if (Objects.isNull(type) || Objects.isNull(listener))
            return;

        if (!StringUtils.startsWith(type.toString(), "class"))
            return;

        Set<Listener<?>> listeners = listenerHub.get(type);
        if (Objects.isNull(listeners)) {
            listeners = new HashSet<>();
            listenerHub.put(type, listeners);
        }

        if (listeners.contains(listener))
            return;
        if (log.isDebugEnabled())
            log.info("事件总线注册事件：{} 监听器：{}", type, listener);
        listeners.add(listener);
    }


    @Override
    public void run(String... args) throws Exception {
        Executor executor;
        String[] executorNames = applicationContext.getBeanNamesForType(Executor.class);
        if (ArrayUtils.isEmpty(executorNames)) {
            executor = Executors.newFixedThreadPool(1);
        } else {
            executor = applicationContext.getBean(executorNames[0], Executor.class);
        }

        String[] names = applicationContext.getBeanNamesForType(Listener.class);
        for (String name : names) {
            Listener<?> bean = applicationContext.getBean(name, Listener.class);
            putListener(bean, executor);
        }
    }

    private static void genericInterfaces(Set<Type> types, Class<?> clazz) {
        if (Objects.isNull(types) || Objects.isNull(clazz))
            return;
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        if (ArrayUtils.isNotEmpty(genericInterfaces))
            types.addAll(Arrays.asList(genericInterfaces));
        Class<?> superclass = clazz.getSuperclass();
        if (Objects.isNull(superclass))
            return;
        if (superclass.isAssignableFrom(Object.class))
            return;
        if (Listener.class.isAssignableFrom(superclass)) {
            types.add(clazz.getGenericSuperclass());
            genericInterfaces(types, superclass);
        }
    }

    public static void putListener(Listener<?> bean, Executor executor) {
        if (bean instanceof BaseAsyncListener<?> baseAsyncListener) {
            baseAsyncListener.setExecutor(executor);
        }

        Class<?> beanClass = bean.getClass();
        if (AopUtils.isAopProxy(bean)) {
            beanClass = AopUtils.getTargetClass(bean);
        }

        Set<Type> genericInterfaces = new HashSet<>();
        genericInterfaces(genericInterfaces, beanClass);
        for (Type type : genericInterfaces) {
            addType(bean, type);
        }
    }

    private static void addType(Listener<?> bean, Type type) {
        if (Objects.isNull(type))
            return;

        if (!(type instanceof ParameterizedType parameterizedType))
            return;

        Type rawType = parameterizedType.getRawType();
        if (!candidateType(rawType))
            return;

        // 获取接口的泛型参数
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (ArrayUtils.isEmpty(actualTypeArguments))
            return;

        for (Type actualTypeArgument : actualTypeArguments) {
            add(actualTypeArgument, bean);
        }
    }

    private static boolean candidateType(Type rawType) {
        if (Objects.isNull(rawType))
            return false;
        String typeName = rawType.getTypeName();
        if (StringUtils.isBlank(typeName))
            return false;
        try {
            Class<?> aClass = Class.forName(typeName);
            return Listener.class.isAssignableFrom(aClass);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static <E> void push(E event) {
        if (Objects.isNull(event))
            return;

        Set<Listener<?>> listeners = listenerHub.get(event.getClass());
        if (CollectionUtils.isEmpty(listeners))
            return;
        for (Listener<?> listener : listeners) {
            //noinspection unchecked
            ((Listener<E>) listener).onEvent(event);
        }
    }
}