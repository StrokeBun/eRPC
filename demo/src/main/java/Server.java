import registry.ServiceDiscovery;
import registry.ServiceRegistry;
import registry.zookeeper.CuratorUtils;
import registry.zookeeper.ZookeeperServiceDiscovery;
import registry.zookeeper.ZookeeperServiceRegistry;
import serialize.JdkSerializer;
import serialize.Serializer;
import stub.ServerStub;

import java.io.IOException;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Server {
    public static void main(String[] args) throws IOException {
        Serializer serializer = new JdkSerializer();
        ServiceRegistry registry = new ZookeeperServiceRegistry("127.0.0.1:2181");
        ServerStub serverStub = new ServerStub(9999, serializer, registry);
        serverStub.register("service.UserService", "service.UserServiceImpl");
        serverStub.run();
    }
}
