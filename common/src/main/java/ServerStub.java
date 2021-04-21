import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public final class ServerStub {
    private boolean running;
    private int port;
    private Map<String, String> registerTable;

    public ServerStub(int port) {
        this.port = port;
        running = true;
        registerTable = new HashMap<>();
    }

    public void register(String interfaceName, String implementName) {
        registerTable.put(interfaceName, implementName);
    }

    public void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while(running){
            Socket client = serverSocket.accept();
            process(client);
            client.close();
        }
        serverSocket.close();
    }

    public void process(Socket client) {
        try (ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream())) {
            Request request = (Request) ois.readObject();
            String className = request.getClassName();
            request.setClassName(registerTable.get(className));
            Response response = getResponse(request);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static Response getResponse(Request request) {
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

    private static Object invoke(Request request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class clazz = Class.forName(className);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod(methodName, request.getParametersType());
        return method.invoke(object, request.getParametersValue());
    }
}
