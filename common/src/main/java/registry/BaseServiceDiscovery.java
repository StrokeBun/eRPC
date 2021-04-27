package registry;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/27
 */
public abstract class BaseServiceDiscovery implements ServiceDiscovery {

    private static Map<String, ServiceDiscovery> discoveryMap = new ConcurrentHashMap<>();

    protected String registryAddress;

    public BaseServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    @Override
    public abstract InetSocketAddress discoverService(String serviceName);
}
