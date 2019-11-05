package com.sculler.core.auth.redis;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisCluster;

@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@ConditionalOnClass({JedisCluster.class})
public class RedisClusterConfigurationProperties {
	List<String> nodes;
	
	int maxtotal;

	int maxidle;
	
	int minidle;

	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}
	
	public int getMaxtotal() {
		return maxtotal;
	}

	public void setMaxtotal(int maxtotal) {
		this.maxtotal = maxtotal;
	}

	public int getMaxidle() {
		return maxidle;
	}

	public void setMaxidle(int maxidle) {
		this.maxidle = maxidle;
	}

	public int getMinidle() {
		return minidle;
	}

	public void setMinidle(int minidle) {
		this.minidle = minidle;
	}
}
