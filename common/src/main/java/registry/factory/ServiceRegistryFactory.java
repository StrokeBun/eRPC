package registry.factory;

import registry.ServiceRegistry;
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
        }
        return null;
    }

    private ServiceRegistryFactory() {

    }
}
