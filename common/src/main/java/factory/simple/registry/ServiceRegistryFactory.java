package factory.simple.registry;

import registry.ServiceRegistry;
import constants.enums.RegistryEnum;
import registry.impl.redis.RedisServiceRegistry;
import registry.impl.zookeeper.ZookeeperServiceRegistry;

/**
 * @description: Simple factory of service registry.
 * @author: Stroke
 * @date: 2021/04/28
 */
public final class ServiceRegistryFactory {

    public static ServiceRegistry newInstance(RegistryEnum type, String address) {
        switch (type) {
            case ZOOKEEPER: {
                return new ZookeeperServiceRegistry(address);
            }
            case REDIS: {
                return new RedisServiceRegistry(address);
            }
        }
        return null;
    }

    private ServiceRegistryFactory() {

    }
}
