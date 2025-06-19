package com.taotao.cloud.ai.alibaba.graph.parallel;

import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.spring.ai.tutorial.graph.parallel.node.ExpanderNode;
import com.spring.ai.tutorial.graph.parallel.node.TranslateNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * @author yingzi
 * @since 2025/6/13
 */
@Configuration
public class ParallelGraphConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ParallelGraphConfiguration.class);

    @Bean
    public StateGraph parallelGraph(ChatClient.Builder chatClientBuilder) throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> {
            HashMap<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();

            // 用户输入
            keyStrategyHashMap.put("query", new ReplaceStrategy());

            keyStrategyHashMap.put("expander_number", new ReplaceStrategy());
            keyStrategyHashMap.put("expander_content", new ReplaceStrategy());

            keyStrategyHashMap.put("translate_language", new ReplaceStrategy());
            keyStrategyHashMap.put("translate_content", new ReplaceStrategy());

            keyStrategyHashMap.put("merge_result", new ReplaceStrategy());

            return keyStrategyHashMap;
        };

        StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                .addNode("expander", node_async(new ExpanderNode(chatClientBuilder)))
                .addNode("translate", node_async(new TranslateNode(chatClientBuilder)))
                .addNode("merge", node_async(new MergeResultsNode()))

                .addEdge(StateGraph.START, "expander")
                .addEdge(StateGraph.START, "translate")
                .addEdge("translate", "merge")
                .addEdge("expander", "merge")

                .addEdge("merge", StateGraph.END);

        // 添加 PlantUML 打印
        GraphRepresentation representation = stateGraph.getGraph(GraphRepresentation.Type.PLANTUML,
                "expander flow");
        logger.info("\n=== expander UML Flow ===");
        logger.info(representation.content());
        logger.info("==================================\n");

        return stateGraph;
    }

    private record MergeResultsNode() implements NodeAction {
        @Override
        public Map<String, Object> apply(OverAllState state) {
            Object expanderContent = state.value("expander_content").orElse("unknown");
            String translateContent = (String) state.value("translate_content").orElse("");

            return Map.of("merge_result", Map.of("expander_content", expanderContent,
                    "translate_content", translateContent));
        }
    }
}
