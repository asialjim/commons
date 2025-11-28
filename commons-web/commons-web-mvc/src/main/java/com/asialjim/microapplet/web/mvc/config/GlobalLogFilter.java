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

package com.asialjim.microapplet.web.mvc.config;

import com.asialjim.microapplet.common.cons.Headers;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * 全局链路追踪组件
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/5/6, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class GlobalLogFilter implements Filter {

    /**
     * 做的过滤器
     *
     * @param servletRequest  servlet请求
     * @param servletResponse servlet响应
     * @param filterChain     过滤器链
     * @throws IOException      无效
     * @throws ServletException 无效
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String sessionId = Optional.ofNullable(request.getHeader(Headers.SessionId)).filter(StringUtils::isNotBlank).orElse("NO-SESSION");
            String traceId = request.getHeader(Headers.TraceId);
            MDC.put(Headers.SessionId, sessionId);
            MDC.put(Headers.TraceId, traceId);
            logRequestHeader(request);
//            ContentCachingResponseWrapper resWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(request, response);

//            byte[] contentAsByteArray = resWrapper.getContentAsByteArray();
//            String json = new String(contentAsByteArray, StandardCharsets.UTF_8);
            logResponseHeader(response);
//            log.info("\r\n<<响应Json: {}", json);
        } finally {
            MDC.clear();
        }
    }

    private void logResponseHeader(HttpServletResponse response) {
        Collection<String> responseHeaders = response.getHeaderNames();
        final StringJoiner headerJ = new StringJoiner("\r\n\t");
        headerJ.add(StringUtils.EMPTY);
        responseHeaders.forEach(name -> headerJ.add(name + "=" + response.getHeader(name)));
        log.info("\r\n<<响应头: {}", headerJ);
    }

    private void logRequestHeader(HttpServletRequest request) {
        final StringJoiner logJ = new StringJoiner("\r\n");
        final String method = request.getMethod();
        final StringBuffer requestURL = request.getRequestURL();
        logJ.add(StringUtils.EMPTY);
        logJ.add(">>请求行: [" + method + "] " + requestURL);

        final StringJoiner headerJ = new StringJoiner("\r\n\t");
        headerJ.add(StringUtils.EMPTY);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headerJ.add(name + "=" + value);
        }
        logJ.add(">>请求头: " + headerJ);
        log.info(logJ.toString());
    }
}