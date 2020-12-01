package com.openapigateway.netty.sync.client;

import com.topinfo.ci.netty.bean.RealDataInfo;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.topinfo.ci.netty.bean.Message;
import com.topinfo.ci.netty.client.NettyClient;
import com.topinfo.ci.netty.client.SyncFuture;
import com.topinfo.ci.netty.service.NettyClientService;
import com.topinfo.ci.netty.utils.AESUtil;

@Service
public class NettyClientServiceImpl implements NettyClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientServiceImpl.class);
    

    //The cache interface here is LoadingCache, which automatically loads the cache when the cache item does not exist.
    private static LoadingCache<String, SyncFuture> futureCache = CacheBuilder.newBuilder()
            //Setting the initial capacity of the cache container to 10
            .initialCapacity(100)
            // maximumSize Sets Cache Size
            .maximumSize(10000)
            //Set the concurrency level to 20, which refers to the number of threads that can write caches at the same time.
            .concurrencyLevel(20)
            // expireAfterWrite expires 8 seconds after the write cache is set
            .expireAfterWrite(8, TimeUnit.SECONDS)
            //Setting Cache Removal Notification
            .removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> notification) {
                    LOGGER.debug("LoadingCache: {} was removed, cause is {}",notification.getKey(), notification.getCause());
                }
            })
            //CacheLoader can be specified in the build method, and caching can be automatically loaded through CacheLoader when caching does not exist.
            .build(new CacheLoader<String, SyncFuture>() {
                @Override
                public SyncFuture load(String key) throws Exception {
                    // When the cache to get the key does not exist, there is no need to add it automatically.
                    return null;
                }
            });
    
    
    @Autowired
    private NettyClient nettyClient;
    
    @Autowired
    private CacheManager cacheManager;
 
    @Override
    public boolean sendMsg(String text, String dataId, String serviceId) {
        
        LOGGER.info("Contents sent:{}", text);

        //TODO        
        //nettyClient.sendMsg(json);
        return true;
    }
 

    @Override
    public String sendSyncMsg(String text, String dataId, String serviceId) {
        
        SyncFuture<String> syncFuture = new SyncFuture<String>();
        // Put it in the cache
        futureCache.put(dataId, syncFuture);
        
        // Encapsulated data
        JSONObject object = new JSONObject();
        object.put("dataId", dataId);
        object.put("text", text);
        
        // Send Synchronized Messages
        String result = nettyClient.sendSyncMsg(object.toJSONString(), syncFuture);
        
        return result;
    }

    @Override
    public void ackSyncMsg(String msg) {
        
        LOGGER.info("ACK Confirmation information: {}",msg);
        
        JSONObject object =JSON.parseObject(msg);
        String dataId = object.getString("dataId");
        
        // Getting data from the cache
        SyncFuture<String> syncFuture = futureCache.getIfPresent(dataId);
        
        // If it is not null, the notification returns
        if(syncFuture != null) {
            syncFuture.setResponse(msg);
        }
    }

}