package factory.simple.registry;

import registry.ServiceDiscovery;
import constants.enums.RegistryEnum;
import registry.impl.redis.RedisServiceDiscovery;
import registry.impl.zookeeper.ZookeeperServiceDiscovery;

/**
 * @description: Simple factory of service discovery.
 * @author: Stroke
 * @date: 2021/04/28
 */
public final class ServiceDiscoveryFactory {

    public static ServiceDiscovery newInstance(RegistryEnum type, String address) {
        switch (type) {
            case ZOOKEEPER: {
                return new ZookeeperServiceDiscovery(address);
            }
            case REDIS: {
                return new RedisServiceDiscovery(address);
            }
        }
        return null;
    }

    private ServiceDiscoveryFactory() {

    }
}
