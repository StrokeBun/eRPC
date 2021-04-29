import entity.User;
import registry.ServiceDiscovery;
import registry.zookeeper.ZookeeperServiceDiscovery;
import serialize.JdkSerializer;
import serialize.Serializer;
import service.UserService;
import stub.ClientStub;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Client {
    public static void main(String[] args) {
        Serializer serializer = new JdkSerializer();
        ServiceDiscovery discovery = new ZookeeperServiceDiscovery("127.0.0.1:2181");
        ClientStub stub = new ClientStub(serializer, discovery);
        UserService service = (UserService) stub.getInstance(UserService.class);
        User user = service.getUser(1, "bzzb");
        if (user != null) {
            System.out.println(user.toString());
        }
    }
}
