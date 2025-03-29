package com.taotao.cloud.cache.support.load;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.common.StringUtils;
import com.taotao.cloud.cache.annotation.CacheInterceptor;
import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheLoad;
import com.taotao.cloud.cache.core.Cache;
import com.taotao.cloud.cache.model.PersistAofEntry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xkzhangsan.time.utils.CollectionUtil;
import com.xkzhangsan.time.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.taotao.boot.common.utils.io.PathUtils.readAllLines;
import static org.dromara.hutool.core.reflect.method.MethodUtil.invoke;

/**
 * 加载策略-AOF文件模式
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheLoadAof<K,V> implements ICacheLoad<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CacheLoadAof.class);

    /**
     * 方法缓存
     *
     * 暂时比较简单，直接通过方法判断即可，不必引入参数类型增加复杂度。
     * @since 2024.06
     */
    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    static {
        Method[] methods = Cache.class.getMethods();

        for(Method method : methods){
            CacheInterceptor cacheInterceptor = method.getAnnotation(CacheInterceptor.class);

            if(cacheInterceptor != null) {
                // 暂时
                if(cacheInterceptor.aof()) {
                    String methodName = method.getName();

                    METHOD_MAP.put(methodName, method);
                }
            }
        }

    }

    /**
     * 文件路径
     * @since 2024.06
     */
    private final String dbPath;

    public CacheLoadAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        List<String> lines = readAllLines(dbPath);
        log.info("[load] 开始处理 path: {}", dbPath);
        if(CollectionUtil.isEmpty(lines)) {
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        for(String line : lines) {
            if(StringUtil.isEmpty(line)) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistAofEntry entry = JSON.parseObject(line, PersistAofEntry.class);

            final String methodName = entry.getMethodName();
            final Object[] objects = entry.getParams();

            final Method method = METHOD_MAP.get(methodName);
            // 反射调用
            invoke(cache, method, objects);
        }
    }

}
