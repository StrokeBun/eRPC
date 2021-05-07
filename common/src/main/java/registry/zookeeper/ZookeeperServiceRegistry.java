package registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import registry.BaseServiceRegistry;

import java.net.InetSocketAddress;

/**
 * @description: register service based on zookeeper
 * @author: Stroke
 * @date: 2021/04/26
 */
public class ZookeeperServiceRegistry extends BaseServiceRegistry {

    public ZookeeperServiceRegistry(String registryServerAddress) {
        super(registryServerAddress);
    }

    @Override
    public void registerService(String serviceName, InetSocketAddress address) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + serviceName + address.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient(registryServerAddress);
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }

    @Override
    public void removeService(String serviceName, InetSocketAddress address) {
        String servicePath = serviceName + address.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient(registryServerAddress);
        CuratorUtils.clearRegistry(zkClient, servicePath);
    }
}
