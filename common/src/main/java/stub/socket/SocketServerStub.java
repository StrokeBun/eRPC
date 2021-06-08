package stub.socket;

import config.RpcServerConfiguration;
import constants.StubConstants;
import dto.Request;
import dto.Response;
import lombok.SneakyThrows;
import serialization.socket.SocketSerializer;
import stub.BaseServerStub;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: Server stub based on socket.
 * @author: Stroke
 * @date: 2021/04/21
 */
public final class SocketServerStub extends BaseServerStub {

    private SocketSerializer serializer = StubConstants.SOCKET_STUB_DEFAULT_SERIALIZER;
    private ExecutorService threadPool;

    public SocketServerStub() {
        super();
        init();
    }

    public SocketServerStub(RpcServerConfiguration configuration) {
        super(configuration);
        init();
    }

    public SocketServerStub(SocketSerializer serializer) {
        super();
        this.serializer = serializer;
        init();
    }

    public SocketServerStub(RpcServerConfiguration configuration, SocketSerializer serializer) {
        super(configuration);
        this.serializer = serializer;
        init();
    }

    private void init() {
        threadPool = Executors.newFixedThreadPool(10);
    }

    @Override
    public void run() throws IOException {
        final int serverPort = configuration.getServerPort();
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while(true){
                Socket client = serverSocket.accept();
                threadPool.submit(new SocketRequestHandler(client));
            }
        } finally {
            threadPool.shutdown();
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
            Request request = serializer.deserialize(socket.getInputStream(), Request.class);
            String className = request.getClassName();
            request.setClassName(registerTable.get(className));
            Response response = getResponse(request);
            serializer.serialize(response, socket.getOutputStream());
            socket.close();
        }
    }
}
