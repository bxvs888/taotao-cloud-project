/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.shell.commond;

import java.util.Locale;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TranslationCommands {

    //	private final TranslationService service;
    //
    //	@Autowired
    //	public TranslationCommands(TranslationService service) {
    //		this.service = service;
    //	}

    @ShellMethod("Translate text from one language to another.")
    public String translate(
            @ShellOption(optOut = true) String text,
            @ShellOption(optOut = true, defaultValue = "en_US") Locale from
            //		@ShellOption(optOut = true) Locate to
            ) {

        // invoke service
        //		return service.translate(text, from, to);
        return "";
    }
}
