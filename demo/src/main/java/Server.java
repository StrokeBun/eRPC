import stub.server.ServerStub;
import stub.server.SocketServerStub;

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
        ServerStub serverStub = new SocketServerStub();
        serverStub.register("UserService", "service.UserServiceImpl");
        serverStub.run();
    }
}
