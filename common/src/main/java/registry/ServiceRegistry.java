package registry;

import java.net.InetSocketAddress;

/**
 * @description: Interface of registering service.
 * @author: Stroke
 * @date: 2021/04/26
 */
public interface ServiceRegistry {

    /**
     * register rpc service.
     * @param serviceName RPC service name
     * @param address service address
     */
    void registerService(String serviceName, InetSocketAddress address);

    void removeService(String serviceName, InetSocketAddress address);
}
