package stub.socket;

import config.RpcServerConfiguration;
import dto.Request;
import dto.Response;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import registry.ServiceRegistry;
import serialize.serializer.iostream.IOStreamSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: the stub of server
 * @author: Stroke
 * @date: 2021/04/21
 */
@AllArgsConstructor
public final class SocketServerStub {

    private RpcServerConfiguration configuration;
    private Map<String, String> registerTable;
    private ExecutorService threadPool;

    public SocketServerStub() {
        configuration = RpcServerConfiguration.builder().build();
        init();
    }

    public SocketServerStub(RpcServerConfiguration configuration) {
        this.configuration = configuration;
        init();
    }

    private void init() {
        registerTable = new ConcurrentHashMap<>();
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void register(String interfaceName, String implementName) throws UnknownHostException {
        registerTable.put(interfaceName, implementName);
        String ip = InetAddress.getLocalHost().getHostAddress();
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        serviceRegistry.registerService(interfaceName, address);
    }

    public void run() throws IOException {
        final int serverPort = configuration.getServerPort();
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            while(true){
                Socket client = serverSocket.accept();
                threadPool.submit(new RequestHandler(client));
            }
        } finally {
            threadPool.shutdown();
            removeService();
        }
    }


    private void removeService() throws UnknownHostException {
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        String ip = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        for (String serviceName : registerTable.keySet()) {
            serviceRegistry.removeService(serviceName, address);
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
            IOStreamSerializer serializer = configuration.getSerializer();
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
