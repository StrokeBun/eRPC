package registry;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/26
 */
public interface ServiceDiscovery {

    /**
     * discover the service
     * @param serviceName RPC service name
     * @return socket address of the service
     */
    InetSocketAddress discoverService(String serviceName);
}
