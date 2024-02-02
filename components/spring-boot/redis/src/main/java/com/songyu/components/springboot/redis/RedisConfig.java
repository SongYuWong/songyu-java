package com.songyu.components.springboot.redis;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * redis 连接配置
 * </p>
 *
 * @author songYu
 * @since 2023/9/21 14:15
 */
@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    @Primary
    public RedisService redisService(RedisConnectionFactory redisConnectionFactory) {
        return new RedisService(jsonValueRedisTemplate(redisConnectionFactory));
    }

    public RedisTemplate<String, Object> jsonValueRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
        log.info("jsonValueRedisTemplate inited");
        return redisTemplate;
    }

    @Bean
    @Primary
    public RedisConnectionFactory myLettuceConnectionFactory() {
        log.info("自定义 redis 连接配置注入");
        try {
            return buildFactory(Duration.ofMinutes(1000), "192.168.101.1:1521", "192.168.101.2:1521");
        } catch (Exception e) {
            log.error("redis连接失败 请检查密码配置", e);
        }
        return null;
    }

    private LettuceConnectionFactory buildFactory(Duration timeout, String... nodes) {
        if (nodes.length > 1) {
            ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                    .enablePeriodicRefresh(Duration.ofSeconds(30)) //按照周期刷新拓扑
                    .enableAllAdaptiveRefreshTriggers() //根据事件刷新拓扑
                    .build();
            ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                    .topologyRefreshOptions(topologyRefreshOptions)
                    .autoReconnect(true)
                    .maxRedirects(3)
                    .build();
            LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                    .clientResources(DefaultClientResources.create())
                    .clientOptions(clusterClientOptions)
                    .shutdownTimeout(timeout)
                    .build();
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
            List<RedisNode> redisNodes = Arrays.stream(nodes).map(n -> {
                String[] split = n.split(":");
                return new RedisNode(split[0], Integer.parseInt(split[1]));
            }).collect(Collectors.toList());
            redisClusterConfiguration.setClusterNodes(redisNodes);
            redisClusterConfiguration.setMaxRedirects(nodes.length);
            redisClusterConfiguration.setPassword(RedisPassword.of("asd"));
            return new LettuceConnectionFactory(redisClusterConfiguration, clientConfiguration);
        } else {
            RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
            String[] split = nodes[0].split(":");
            configuration.setHostName(split[0]);
            configuration.setPort(Integer.parseInt(split[1]));
            configuration.setPassword("asd");
            return new LettuceConnectionFactory(configuration);
        }
    }

}
