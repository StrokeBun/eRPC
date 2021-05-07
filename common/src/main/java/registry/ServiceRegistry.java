package registry;

import java.net.InetSocketAddress;

public interface ServiceRegistry {

    /**
     * register rpc service.
     * @param serviceName RPC service name
     * @param address service address
     */
    void registerService(String serviceName, InetSocketAddress address);

    void removeService(String serviceName, InetSocketAddress address);
}
