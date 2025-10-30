# Commons

![Apache License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
![Java Version](https://img.shields.io/badge/java-21-orange.svg)
![Maven](https://img.shields.io/badge/maven-3.6.0-green.svg)

## 项目简介

Commons是一个通用的Java组件库，为Micro Bank微服务体系提供标准化的基础功能支持。该项目旨在通过封装常用功能，减少重复开发，提高代码复用率，确保微服务架构下各系统的一致性和可靠性。

## 核心功能模块

### 1. commons-common

通用核心功能模块，提供应用基础设施：
- 统一应用启动器（App类）
- 便捷的Spring Bean管理工具
- 基础上下文和结果封装（Res、Result等）
- 异常处理（RsEx）
- 事件监听机制
- 通用工具类集合

### 2. commons-web

Web开发支持模块，包含多个子模块：
- **commons-web-mvc**: Spring MVC增强支持，提供全局异常处理、请求/响应切面等
- **commons-web-flux**: Spring WebFlux支持
- **commons-web-feign**: 服务间通信增强
- **commons-web-nacos**: Nacos服务发现支持
- **commons-web-tomcat/undertow**: 容器适配支持

### 3. commons-cache

缓存功能模块，支持多种缓存策略：
- **commons-cache-caffeine**: 基于Caffeine的本地缓存实现
- **commons-cache-redis**: Redis分布式缓存实现，支持动态TTL
- **commons-cache-caffeine-redis**: 两级缓存策略（本地+分布式）实现

### 4. commons-config

配置管理模块：
- **commons-config-core**: 核心配置管理功能
- **commons-config-mybatis-flex**: MyBatis-Flex配置支持

### 5. commons-crypt

加密解密功能模块：
- **commons-crypt-core**: 核心加密功能，支持SM2等算法
- 证书管理服务

### 6. commons-mybatis

ORM框架增强模块：
- **commons-mybatis-flex**: MyBatis-Flex增强支持
- **commons-mybatis-plus**: MyBatis-Plus增强支持

### 7. commons-security

安全功能模块：
- **commons-security-core**: 核心安全功能
- **commons-security-web**: Web安全支持

## 技术栈

- Java 21
- Spring Boot 3.2.9
- Spring Cloud 2023.0.0
- Spring Cloud Alibaba 2025.0.0.0-preview
- MyBatis-Flex 1.11.1
- MyBatis-Plus 3.5.14
- Hutool 5.8.39
- Apache Commons

## 核心特性

1. **统一应用启动管理**
   ```java
   // 简单启动示例
   App.voidStart(MyApplication.class, args);
   
   // 带应用名称启动
   App.voidStart("my-application", MyApplication.class, args);
   ```

2. **便捷的Bean管理**
   ```java
   // 获取Bean或返回null
   UserService userService = App.beanOrNull(UserService.class);
   
   // 获取Bean或抛出指定异常
   UserService userService = App.beanOrThrow(UserService.class, () -> new ServiceNotFoundException("UserService not found"));
   
   // 获取Bean并执行操作
   App.beanAndThen(UserService.class, service -> service.initialize());
   ```

3. **统一的结果封装**
   - 使用Res和Result类统一API响应格式
   - 全局异常处理和响应增强

4. **灵活的缓存策略**
   - 支持本地缓存、分布式缓存和两级缓存
   - 动态TTL配置支持

5. **配置管理增强**
   - 支持多环境配置
   - 配置属性自动注入

## 使用指南

### 引入依赖

在项目的pom.xml中添加相应模块的依赖：

```xml
<dependency>
    <groupId>com.asialjim.microapplet</groupId>
    <artifactId>commons-common</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

### 基础配置

1. 在启动类上无需特殊配置，使用App类启动即可
2. 根据需要引入其他子模块依赖

## 许可证

本项目采用Apache License 2.0开源许可证。详见[LICENSE](LICENSE)文件。

## 开发者信息

- **开发者**: Asial Jim
- **邮箱**: asialjim@qq.com
- **GitHub**: https://github.com/MicroApplet/commons

## 贡献指南

欢迎提交Issue和Pull Request来改进本项目。

## 版本历史

- **2.0.0-SNAPSHOT**: 当前开发版本