package registry;

import exception.RpcException;
import exception.enums.RpcErrorMessageEnum;
import loadbalance.DefaultLoadBalance;
import loadbalance.LoadBalance;

import java.net.InetSocketAddress;
import java.util.List;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/27
 */
public abstract class BaseServiceDiscovery implements ServiceDiscovery {

    protected String registryServerAddress;
    protected LoadBalance loadBalance;

    public BaseServiceDiscovery(String registryServerAddress) {
        this (registryServerAddress, new DefaultLoadBalance());
    }

    public BaseServiceDiscovery(String registryServerAddress, LoadBalance loadBalance) {
        this.registryServerAddress = registryServerAddress;
        this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress discoverService(String serviceName) {
        List<String> serviceUrlList = getServiceUrlList(serviceName);
        checkUrls(serviceUrlList);
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

    protected abstract List<String> getServiceUrlList(String serviceName);

    private void checkUrls(List<String> urls) {
        if (urls == null || urls.size() == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CANT_BE_FOUND);
        }
    }
}
