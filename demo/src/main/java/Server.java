import registry.ServiceRegistry;
import registry.redis.RedisServiceRegistry;
import registry.zookeeper.ZookeeperServiceRegistry;
import serialize.JdkSerializer;
import serialize.Serializer;
import stub.socket.ServerStub;

import java.io.IOException;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Server {
    public static void main(String[] args) throws IOException {
        //Serializer serializer = new JdkSerializer();
        //ServiceRegistry registry = new RedisServiceRegistry("localhost");
        //ServerStub serverStub = new ServerStub(9999, serializer, registry);
        ServerStub serverStub = new ServerStub();
        serverStub.register("service.UserService", "service.UserServiceImpl");
        serverStub.run();
    }
}
