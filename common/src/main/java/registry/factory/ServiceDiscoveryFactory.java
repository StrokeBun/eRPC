package registry.factory;

import registry.ServiceDiscovery;
import registry.zookeeper.ZookeeperServiceDiscovery;

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
        }
        return null;
    }

    private ServiceDiscoveryFactory() {

    }
}
