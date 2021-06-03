package stub;

import config.RpcServerConfiguration;
import dto.Request;
import dto.Response;
import registry.ServiceRegistry;
import util.InvokeUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: A skeletal implementation of the {@link ServerStub}.
 * @author: Stroke
 * @date: 2021/05/12
 */
public abstract class BaseServerStub implements ServerStub {

    protected RpcServerConfiguration configuration ;
    protected Map<String, String> registerTable;

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
    public abstract void run() throws Exception;

    @Override
    public void register(String interfaceName, String implementName) throws UnknownHostException {
        registerTable.put(interfaceName, implementName);
        String ip = InetAddress.getLocalHost().getHostAddress();
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        serviceRegistry.registerService(interfaceName, address);
    }

    protected void removeService() throws UnknownHostException {
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        String ip = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        for (String serviceName : registerTable.keySet()) {
            serviceRegistry.removeService(serviceName, address);
        }
    }

    /**
     * Generate the RPC response.
     * @param request RPC request from client
     * @return RPC response
     */
    public Response getResponse(Request request) {
        Object result = null;
        Response response = new Response();
        response.setResponseId(request.getRequestId());
        try {
            result = InvokeUtils.invoke(request);
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

    public Map<String, String> getRegisterTable() {
        return new HashMap<>(registerTable);
    }

}
