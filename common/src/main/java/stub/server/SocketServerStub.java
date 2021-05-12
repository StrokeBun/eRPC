package stub.server;

import config.RpcServerConfiguration;
import dto.Request;
import dto.Response;
import lombok.SneakyThrows;
import serialize.factory.SingletonIOStreamSerializerFactory;
import serialize.serializer.iostream.IOStreamSerializer;

import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: server stub based on socket
 * @author: Stroke
 * @date: 2021/04/21
 */
public final class SocketServerStub extends BaseServerStub {

    private static IOStreamSerializer DEFAULT_SERIALIZER = SingletonIOStreamSerializerFactory.getSerializer("jdk");
    private IOStreamSerializer serializer = DEFAULT_SERIALIZER;
    private ExecutorService threadPool;

    public SocketServerStub() {
        super();
        init();
    }

    public SocketServerStub(RpcServerConfiguration configuration) {
        super(configuration);
        init();
    }

    public SocketServerStub(IOStreamSerializer serializer) {
        super();
        this.serializer = serializer;
        init();
    }

    public SocketServerStub(RpcServerConfiguration configuration, IOStreamSerializer serializer) {
        super(configuration);
        this.serializer = serializer;
        init();
    }

    private void init() {
        threadPool = Executors.newFixedThreadPool(10);
    }

    @Override
    public void run() throws IOException {
        final RpcServerConfiguration configuration = getConfiguration();
        final int serverPort = configuration.getServerPort();
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while(true){
                Socket client = serverSocket.accept();
                threadPool.submit(new SocketRequestHandler(client));
            }
        } finally {
            threadPool.shutdown();
            removeService();
        }
    }

    private class SocketRequestHandler implements Runnable {
        private Socket socket;
        public SocketRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @SneakyThrows
        @Override
        public void run() {
            final Map<String, String> registerTable = getRegisterTable();
            Request request = serializer.deserialize(socket.getInputStream(), Request.class);
            String className = request.getClassName();
            request.setClassName(registerTable.get(className));
            Response response = getResponse(request);
            serializer.serialize(response, socket.getOutputStream());
            socket.close();
        }
    }
}
