package com.taotao.cloud.ai.alibaba.graph.parallel;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yingzi
 * @since 2025/6/13
 */
@RestController
@RequestMapping("/graph/parallel")
public class ParallelController {

    private static final Logger logger = LoggerFactory.getLogger(ParallelController.class);

    private final CompiledGraph compiledGraph;

    public ParallelController(@Qualifier("parallelGraph") StateGraph stateGraph) throws GraphStateException {
        this.compiledGraph = stateGraph.compile();
    }

    @GetMapping(value = "/expand-translate")
    public Map<String, Object> expandAndTranslate(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？", required = false) String query,
                                      @RequestParam(value = "expander_number", defaultValue = "3", required = false) Integer  expanderNumber,
                                      @RequestParam(value = "translate_language", defaultValue = "english", required = false) String translateLanguage,
                                      @RequestParam(value = "thread_id", defaultValue = "yingzi", required = false) String threadId){
        RunnableConfig runnableConfig = RunnableConfig.builder().threadId(threadId).build();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("query", query);
        objectMap.put("expander_number", expanderNumber);
        objectMap.put("translate_language", translateLanguage);

        Optional<OverAllState> invoke = this.compiledGraph.invoke(objectMap, runnableConfig);

        return invoke.map(OverAllState::data).orElse(new HashMap<>());
    }

}
