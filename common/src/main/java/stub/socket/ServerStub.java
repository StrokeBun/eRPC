package stub.socket;

import config.RpcServerConfiguration;
import dto.Request;
import dto.Response;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import registry.ServiceRegistry;
import serialize.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: the stub of server
 * @author: Stroke
 * @date: 2021/04/21
 */
@AllArgsConstructor
public final class ServerStub {

    private int serverPort;
    private Serializer serializer;
    private ServiceRegistry serviceRegistry;
    private Map<String, String> registerTable;
    private ExecutorService threadPool;

    public ServerStub() {
        serverPort = RpcServerConfiguration.getRpcServerPort();
        serializer = RpcServerConfiguration.getSerializer();
        serviceRegistry = RpcServerConfiguration.getDefaultServiceRegistry();
        init();
    }

    public ServerStub(int serverPort, Serializer serializer, ServiceRegistry serviceRegistry) {
        this.serverPort = serverPort;
        this.serializer = serializer;
        this.serviceRegistry = serviceRegistry;
        init();
    }

    private void init() {
        registerTable = new HashMap<>();
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void register(String interfaceName, String implementName) throws UnknownHostException {
        registerTable.put(interfaceName, implementName);
        String ip = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        serviceRegistry.registerService(interfaceName, address);
    }

    public void run() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while(true){
                Socket client = serverSocket.accept();
                threadPool.submit(new RequestHandler(client));
            }
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * Handle the RPC request.
     */
    private class RequestHandler implements Runnable {
        private Socket socket;

        public RequestHandler(Socket socket) {
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

        /**
         * Generate the RPC response.
         * @param request RPC request from client
         * @return RPC response
         */
        private Response getResponse(Request request) {
            Object result = null;
            Response response = new Response();
            response.setResponseId(request.getRequestId());
            try {
                result = invoke(request);
            } catch (ClassNotFoundException e) {
                response.setError("class not found");
            } catch (NoSuchMethodException e) {
                response.setError("method not found");
            } catch (Exception e){
                response.setError("function inner error");
            } finally {
                response.setResult(result);
            }
            return response;
        }

        /**
         * Use reflection to invoke the method.
         * @param request RPC request from client
         * @return result object
         */
        private Object invoke(Request request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
            String className = request.getClassName();
            String methodName = request.getMethodName();
            Class clazz = Class.forName(className);
            Object object = clazz.newInstance();
            Method method = clazz.getMethod(methodName, request.getParametersType());
            return method.invoke(object, request.getParametersValue());
        }

    }
}
