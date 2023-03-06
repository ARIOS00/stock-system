package com.example.stock.config;


import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisClusterConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Value("${redis.maxRedirects:3}")
    private int maxRedirects;

    @Value("${redis.refreshTime:5}")
    private int refreshTime;

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
//
//        redisClusterConfiguration.setMaxRedirects(maxRedirects);
//
//        //支持自适应集群拓扑刷新和静态刷新源
//        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =  ClusterTopologyRefreshOptions.builder()
//                .enablePeriodicRefresh()
//                .enableAllAdaptiveRefreshTriggers()
//                .refreshPeriod(Duration.ofSeconds(refreshTime))
//                .build();
//
//        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
//                .topologyRefreshOptions(clusterTopologyRefreshOptions).build();
//
//        //从优先，读写分离，读从可能存在不一致，最终一致性CP
//        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
//                .readFrom(ReadFrom.SLAVE_PREFERRED)
//                .clientOptions(clusterClientOptions).build();
//
//        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}


