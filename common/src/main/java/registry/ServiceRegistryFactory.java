package registry;

import registry.redis.RedisServiceRegistry;
import registry.zookeeper.ZookeeperServiceRegistry;

/**
 * @description: Simple factory of service registry.
 * @author: Stroke
 * @date: 2021/04/28
 */
public class ServiceRegistryFactory {

    public static ServiceRegistry newInstance(String type, String address) {
        switch (type) {
            case "zk": {
                return new ZookeeperServiceRegistry(address);
            }
            case "redis": {
                return new RedisServiceRegistry(address);
            }
        }
        return null;
    }

    private ServiceRegistryFactory() {

    }
}
