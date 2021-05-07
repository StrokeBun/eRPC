import entity.User;
import org.checkerframework.checker.units.qual.C;
import registry.ServiceDiscovery;
import registry.ServiceRegistry;
import registry.redis.RedisServiceDiscovery;
import registry.redis.RedisServiceRegistry;
import registry.zookeeper.ZookeeperServiceDiscovery;
import serialize.JdkSerializer;
import serialize.Serializer;
import service.UserService;
import stub.socket.ClientStub;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Client {
    public static void main(String[] args) {
        //Serializer serializer = new JdkSerializer();
        //ServiceDiscovery discovery = new RedisServiceDiscovery("localhost");
        //ClientStub stub = new ClientStub(serializer, discovery);
        ClientStub stub = new ClientStub();
        UserService service = stub.getInstance(UserService.class);

        User user = service.getUser(1, "bzzb");
        if (user != null) {
            System.out.println(user.toString());
        }
    }
}
