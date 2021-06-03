package registry;

import java.net.InetSocketAddress;

/**
 * @description: A skeletal implementation of the {@link ServiceRegistry}
 * @author: Stroke
 * @date: 2021/04/27
 */
public abstract class BaseServiceRegistry implements ServiceRegistry {

    protected String registryServerAddress;

    public BaseServiceRegistry(String registryServerAddress) {
        this.registryServerAddress = registryServerAddress;
    }

    @Override
    public abstract void registerService(String serviceName, InetSocketAddress address);

    @Override
    public abstract void removeService(String serviceName, InetSocketAddress address);
}
