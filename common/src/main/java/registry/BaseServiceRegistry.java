package registry;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/27
 */
public abstract class BaseServiceRegistry implements ServiceRegistry {

    protected String registryAddress;

    public BaseServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public abstract void registerService(String serviceName, InetSocketAddress address);

}
