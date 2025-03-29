package com.taotao.cloud.cache.support.persist;

import com.taotao.boot.common.utils.common.StringUtils;
import com.taotao.boot.common.utils.io.FileUtils;
import com.taotao.cloud.cache.api.ICache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.xkzhangsan.time.utils.StringUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.taotao.boot.common.utils.io.FileUtils.createFile;
import static com.taotao.boot.common.utils.io.FileUtils.exists;

/**
 * 缓存持久化-AOF 持久化模式
 * @author shuigedeng
 * @since 2024.06
 */
public class CachePersistAof<K,V> extends CachePersistAdaptor<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CachePersistAof.class);

    /**
     * 缓存列表
     * @since 2024.06
     */
    private final List<String> bufferList = new ArrayList<>();

    /**
     * 数据持久化路径
     * @since 2024.06
     */
    private final String dbPath;

    public CachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        log.info("开始 AOF 持久化到文件");
        // 1. 创建文件
        if(!exists(dbPath)) {
            createFile(dbPath);
        }
        // 2. 持久化追加到文件中
        FileUtils.append(dbPath, bufferList);

        // 3. 清空 buffer 列表
        bufferList.clear();
        log.info("完成 AOF 持久化到文件");
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * 添加文件内容到 buffer 列表中
     * @param json json 信息
     * @since 2024.06
     */
    public void append(final String json) {
        if(StringUtil.isNotEmpty(json)) {
            bufferList.add(json);
        }
    }

}
