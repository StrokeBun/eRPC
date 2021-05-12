import entity.User;
import service.UserService;
import stub.client.ClientStub;
import stub.client.SocketClientStub;

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
        ClientStub stub = new SocketClientStub();
        UserService service = stub.getInstance(UserService.class);

        User user = service.getUser(1, "bzzb");
        if (user != null) {
            System.out.println(user.toString());
        }
    }
}
