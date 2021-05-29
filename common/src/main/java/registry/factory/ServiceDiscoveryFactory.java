package registry.factory;

import registry.ServiceDiscovery;
import registry.impl.redis.RedisServiceDiscovery;
import registry.impl.zookeeper.ZookeeperServiceDiscovery;

/**
 * @description: Simple factory of service discovery.
 * @author: Stroke
 * @date: 2021/04/28
 */
public class ServiceDiscoveryFactory {

    public static ServiceDiscovery newInstance(String type, String address) {
        switch (type) {
            case "zk": {
                return new ZookeeperServiceDiscovery(address);
            }
            case "redis": {
                return new RedisServiceDiscovery(address);
            }
        }
        return null;
    }

    private ServiceDiscoveryFactory() {

    }
}
