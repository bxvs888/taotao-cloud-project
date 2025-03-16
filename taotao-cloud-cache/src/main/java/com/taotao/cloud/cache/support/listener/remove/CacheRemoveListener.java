package com.taotao.cloud.cache.support.listener.remove;

import com.taotao.cloud.cache.api.ICacheRemoveListener;
import com.taotao.cloud.cache.api.ICacheRemoveListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 默认的删除监听类
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type: {}",
                context.key(), context.value(), context.type());
    }

}
