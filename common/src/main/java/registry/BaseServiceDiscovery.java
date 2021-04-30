package registry;

import exception.RpcException;
import exception.enums.RpcErrorMessageEnum;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/27
 */
public abstract class BaseServiceDiscovery implements ServiceDiscovery {

    protected String registryServerAddress;

    public BaseServiceDiscovery(String registryServerAddress) {
        this.registryServerAddress = registryServerAddress;
    }

    @Override
    public abstract InetSocketAddress discoverService(String serviceName);

    protected void checkUrls(List<String> urls) {
        if (urls == null || urls.size() == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CANT_BE_FOUND);
        }
    }
}
