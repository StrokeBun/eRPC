package registry.impl.redis;

import io.lettuce.core.RedisClient;
import registry.BaseServiceRegistry;
import registry.utils.RedisUtils;

import java.net.InetSocketAddress;

/**
 * @description: register service based on redis
 * @author: Stroke
 * @date: 2021/04/30
 */
public class RedisServiceRegistry extends BaseServiceRegistry {

    public RedisServiceRegistry(String registryServerAddress) {
        super(registryServerAddress);
    }

    @Override
    public void registerService(String serviceName, InetSocketAddress address) {
        RedisClient redisClient = RedisUtils.getRedisClient(registryServerAddress);
        RedisUtils.add(redisClient, serviceName, address.toString().substring(1));
    }

    @Override
    public void removeService(String serviceName, InetSocketAddress address) {
        RedisClient redisClient = RedisUtils.getRedisClient(registryServerAddress);
        RedisUtils.remove(redisClient, serviceName, address.toString().substring(1));
    }
}
