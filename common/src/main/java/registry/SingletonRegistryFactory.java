package registry;

import registry.zookeeper.ZookeeperServiceDiscovery;
import registry.zookeeper.ZookeeperServiceRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/26
 */
public class SingletonRegistryFactory {

    private static Map<String, ServiceDiscovery> discoveryMap = new ConcurrentHashMap<>();
    private static Map<String, ServiceRegistry> registryMap = new ConcurrentHashMap<>();
    static {
        // init service discovery
        discoveryMap.put("zookeeper", new ZookeeperServiceDiscovery());

        // init registry discovery
        registryMap.put("zookeeper", new ZookeeperServiceRegistry());
    }
    public static ServiceDiscovery getServiceDiscoveryInstance(String type) {
        return discoveryMap.get(type);
    }

    public static ServiceRegistry getServiceRegistryInstance(String type) {
        return registryMap.get(type);
    }

    private SingletonRegistryFactory() {

    }
}
