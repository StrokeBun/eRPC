import java.io.IOException;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerStub serverStub = new ServerStub(9999);
        serverStub.register("service.UserService", "service.UserServiceImpl");
        serverStub.run();
    }
}
