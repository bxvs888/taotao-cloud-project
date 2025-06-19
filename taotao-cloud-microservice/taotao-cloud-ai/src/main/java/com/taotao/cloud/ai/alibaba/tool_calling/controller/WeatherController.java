package com.taotao.cloud.ai.alibaba.tool;

import com.spring.ai.tutorial.toolcall.component.weather.WeatherProperties;
import com.spring.ai.tutorial.toolcall.component.weather.method.WeatherTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yingzi
 * @date 2025/3/25:15:30
 */
@RestController
@RequestMapping("/chat/weather")
public class WeatherController {

    private final ChatClient chatClient;

    private final WeatherProperties weatherProperties;


    public WeatherController(ChatClient.Builder chatClientBuilder, WeatherProperties weatherProperties) {
        this.chatClient = chatClientBuilder.build();
        this.weatherProperties = weatherProperties;
    }

    /**
     * 无工具版
     */
    @GetMapping("/call")
    public String call(@RequestParam(value = "query", defaultValue = "请告诉我北京1天以后的天气") String query) {
        return chatClient.prompt(query).call().content();
    }

    /**
     * 调用工具版 - function
     */
    @GetMapping("/call/tool-function")
    public String callToolFunction(@RequestParam(value = "query", defaultValue = "请告诉我北京1天以后的天气") String query) {
        return chatClient.prompt(query).toolNames("getWeatherFunction").call().content();
    }

    /**
     * 调用工具版 - method
     */
    @GetMapping("/call/tool-method")
    public String callToolMethod(@RequestParam(value = "query", defaultValue = "请告诉我北京1天以后的天气") String query) {
        return chatClient.prompt(query).tools(new WeatherTools(weatherProperties)).call().content();
    }
}
