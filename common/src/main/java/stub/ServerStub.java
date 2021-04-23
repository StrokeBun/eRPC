package stub;

import config.RpcServerConfiguration;
import dto.Request;
import dto.Response;
import lombok.SneakyThrows;
import serialize.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: the stub of server
 * @author: Stroke
 * @date: 2021/04/21
 */
public final class ServerStub {

    private boolean running;
    private Map<String, String> registerTable;
    private Serializer serializer;
    private ExecutorService threadPool;

    public ServerStub() {
        running = true;
        registerTable = new HashMap<>();
        serializer = RpcServerConfiguration.getSerializer();
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void register(String interfaceName, String implementName) {
        registerTable.put(interfaceName, implementName);
    }

    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(RpcServerConfiguration.getRpcServerPort());
        while(running){
            Socket client = serverSocket.accept();
            threadPool.submit(new RequestHandler(client));
        }
        serverSocket.close();
    }

    /**
     * Handle the RPC request.
     */
    public class RequestHandler implements Runnable {
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
         * @return
         * @throws ClassNotFoundException
         * @throws NoSuchMethodException
         * @throws IllegalAccessException
         * @throws InstantiationException
         * @throws InvocationTargetException
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
