import stub.ServerStub;

import java.io.IOException;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerStub serverStub = ServerStub.getStub();
        serverStub.register("service.UserService", "service.UserServiceImpl");
        serverStub.run();
    }
}
