package stub.server;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @description: interface of server stub
 * @author: Stroke
 * @date: 2021/05/12
 */
public interface ServerStub {
    void register(String interfaceName, String implementName) throws UnknownHostException;
    void run() throws IOException;
}
