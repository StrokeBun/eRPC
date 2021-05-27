import stub.ServerStub;
import stub.netty.server.NettyServerStub;
import stub.socket.SocketServerStub;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Server {
    public static void main(String[] args) throws Exception {
        //Serializer serializer = new JdkSerializer();
        //ServiceRegistry registry = new RedisServiceRegistry("localhost");
        //ServerStub serverStub = new ServerStub(9999, serializer, registry);
        ServerStub serverStub = new NettyServerStub();
        serverStub.register("UserService", "service.UserServiceImpl");
        serverStub.run();
    }
}
