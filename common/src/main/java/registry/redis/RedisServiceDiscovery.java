package registry.redis;

import io.lettuce.core.RedisClient;
import registry.BaseServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/30
 */
public class RedisServiceDiscovery extends BaseServiceDiscovery {

    public RedisServiceDiscovery(String registryServerAddress) {
        super(registryServerAddress);
    }

    @Override
    public InetSocketAddress discoverService(String serviceName) {
        RedisClient redisClient = RedisUtils.getRedisClient(registryServerAddress);
        List<String> serviceUrlList = new ArrayList<>(RedisUtils.get(redisClient, serviceName));
        checkUrls(serviceUrlList);

        // todo: load balance
        String targetServiceUrl = serviceUrlList.get(0);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0].substring(1);
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
