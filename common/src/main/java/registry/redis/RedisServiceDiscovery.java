package registry.redis;

import io.lettuce.core.RedisClient;
import loadbalance.LoadBalance;
import registry.BaseServiceDiscovery;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: discover service based on redis
 * @author: Stroke
 * @date: 2021/04/30
 */
public class RedisServiceDiscovery extends BaseServiceDiscovery {

    public RedisServiceDiscovery(String registryServerAddress) {
        super(registryServerAddress);
    }

    public RedisServiceDiscovery(String registryServerAddress, LoadBalance loadBalance) {
        super(registryServerAddress, loadBalance);
    }

    @Override
    protected List<String> getServiceUrlList(String serviceName) {
        RedisClient redisClient = RedisUtils.getRedisClient(registryServerAddress);
        return new ArrayList<>(RedisUtils.get(redisClient, serviceName));
    }
}
