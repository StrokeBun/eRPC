package stub;

import config.RpcServerConfiguration;
import registry.ServiceRegistry;

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

    protected RpcServerConfiguration configuration ;
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
    public abstract void run() throws Exception;

    protected void removeService() throws UnknownHostException {
        final int serverPort = configuration.getServerPort();
        final ServiceRegistry serviceRegistry = configuration.getServiceRegistry();
        String ip = InetAddress.getLocalHost().getHostAddress();
        InetSocketAddress address = new InetSocketAddress(ip, serverPort);
        for (String serviceName : registerTable.keySet()) {
            serviceRegistry.removeService(serviceName, address);
        }
    }

    protected Map<String, String> getRegisterTable() {
        return registerTable;
    }

}
