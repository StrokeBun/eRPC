package stub.server;

import config.RpcServerConfiguration;
import dto.Request;
import dto.Response;
import registry.ServiceRegistry;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: base implement of server stub
 * @author: Stroke
 * @date: 2021/05/12
 */
public abstract class BaseServerStub implements ServerStub {

    private RpcServerConfiguration configuration ;
    private Map<String, String> registerTable;

    public BaseServerStub() {
        configuration = RpcServerConfiguration.builder().build();
        init();
    }

    public BaseServerStub(RpcServerConfiguration configuration) {
        this.configuration = configuration;
        init();
    }

    private void init() {
        registerTable = new ConcurrentHashMap<>();
    }

    @Override
    public void register(String interfaceName, String implementName) throws UnknownHostException {
        registerTable.put(interfaceName, implementName);
        String ip = InetAddress.getLocalHost().getHostAddress();
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        serviceRegistry.registerService(interfaceName, address);
    }

    @Override
    public abstract void run() throws IOException;

    protected void removeService() throws UnknownHostException {
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        String ip = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        for (String serviceName : registerTable.keySet()) {
            serviceRegistry.removeService(serviceName, address);
        }
    }

    protected RpcServerConfiguration getConfiguration() {
        return configuration;
    }

    protected Map<String, String> getRegisterTable() {
        return registerTable;
    }

    /**
     * Generate the RPC response.
     * @param request RPC request from client
     * @return RPC response
     */
    protected Response getResponse(Request request) {
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
    protected Object invoke(Request request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class clazz = Class.forName(className);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod(methodName, request.getParametersType());
        return method.invoke(object, request.getParametersValue());
    }

}
