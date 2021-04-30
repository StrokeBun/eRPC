package registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import registry.BaseServiceDiscovery;
import registry.ServiceDiscovery;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @description: discover service based on zookeeper
 * @author: Stroke
 * @date: 2021/04/26
 */
public class ZookeeperServiceDiscovery extends BaseServiceDiscovery implements ServiceDiscovery {

    public ZookeeperServiceDiscovery(String registryServerAddress) {
        super(registryServerAddress);
    }

    @Override
    public InetSocketAddress discoverService(String serviceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient(registryServerAddress);
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        checkUrls(serviceUrlList);

        // load balance
        String targetServiceUrl = serviceUrlList.get(0);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
