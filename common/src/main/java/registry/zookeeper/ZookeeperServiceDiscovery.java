package registry.zookeeper;

import loadbalance.LoadBalance;
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

    public ZookeeperServiceDiscovery(String registryServerAddress, LoadBalance loadBalance) {
        super(registryServerAddress, loadBalance);
    }

    @Override
    protected List<String> getServiceUrlList(String serviceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient(registryServerAddress);
        return CuratorUtils.getChildrenNodes(zkClient, serviceName);
    }
}
