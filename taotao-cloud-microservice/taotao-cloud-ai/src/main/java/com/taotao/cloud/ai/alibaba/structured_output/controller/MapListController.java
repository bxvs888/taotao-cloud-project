package com.taotao.cloud.ai.alibaba.structured;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientAttributes;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author yingzi
 * @date 2025/5/22 22:18
 */

@RestController
@RequestMapping("/map-list")
public class MapListController {

    private static final Logger logger = LoggerFactory.getLogger(MapListController.class);

    private final ChatClient chatClient;
    private final MapOutputConverter mapConverter;
    private final ListOutputConverter listConverter;

    public MapListController(ChatClient.Builder builder) {
        // map转换器
        this.mapConverter = new MapOutputConverter();
        // list转换器
        this.listConverter = new ListOutputConverter(new DefaultConversionService());

        this.chatClient = builder
                .build();
    }

    @GetMapping("/map")
    public Map<String, Object> map(@RequestParam(value = "query", defaultValue = "请为我描述下影子的特性") String query) {
        return chatClient.prompt(query)
                .advisors(
                        a -> a.param(ChatClientAttributes.OUTPUT_FORMAT.getKey(), mapConverter.getFormat())
                ).call().entity(mapConverter);
    }

    @GetMapping("/list")
    public List<String> list(@RequestParam(value = "query", defaultValue = "请为我描述下影子的特性") String query) {
        return chatClient.prompt(query)
                .advisors(
                        a -> a.param(ChatClientAttributes.OUTPUT_FORMAT.getKey(), listConverter.getFormat())
                ).call().entity(listConverter);
    }
}
