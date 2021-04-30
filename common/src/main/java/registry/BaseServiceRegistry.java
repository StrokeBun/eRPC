package registry;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/27
 */
public abstract class BaseServiceRegistry implements ServiceRegistry {

    protected String registryServerAddress;

    public BaseServiceRegistry(String registryServerAddress) {
        this.registryServerAddress = registryServerAddress;
    }

    public abstract void registerService(String serviceName, InetSocketAddress address);

}
