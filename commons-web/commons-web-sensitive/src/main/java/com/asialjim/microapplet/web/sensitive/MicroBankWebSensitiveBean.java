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

package com.asialjim.microapplet.web.sensitive;

import com.asialjim.microapplet.web.sensitive.handler.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 微银 WebMVC Spring Bean 扫描
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/4, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
@ComponentScan
@Import({
        BankCardSensitiveHandler.class,
        ChineseCitizenIdCardSensitiveHandler.class,
        ChineseMobilePhoneSensitiveHandler.class,
        ChineseNameSensitiveHandler.class,
        ChineseTellPhoneSensitiveHandler.class,
        CustomerSensitiveHandler.class,
        EMailSensitiveHandler.class,
        EnglishNameSensitiveHandler.class
})
public class MicroBankWebSensitiveBean {

}