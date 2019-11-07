package com.sculler.core.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.sculler.core.auth.redis.RedisClusterConfigurationProperties;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	
	@Autowired
	RedisClusterConfigurationProperties rdsClusterConfigProperties;
	
	@Bean
	public RedisConnectionFactory rdsConnectionFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(rdsClusterConfigProperties.getMaxtotal());
		jedisPoolConfig.setMaxIdle(rdsClusterConfigProperties.getMaxidle());
		jedisPoolConfig.setMinIdle(rdsClusterConfigProperties.getMinidle());
		return new JedisConnectionFactory(new RedisClusterConfiguration(rdsClusterConfigProperties.getNodes()), jedisPoolConfig);
	}
	
	@Bean
	public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory redisConnectionfactory) {
		RedisTemplate<String, String> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionfactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
	}
	
	@Bean
	public RedisTemplate<String, Object> redisObjectTemplate(RedisConnectionFactory redisConnectionfactory) {
		RedisTemplate<String, Object> redisTemplate=new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionfactory);
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

}
