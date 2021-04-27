package stub;

import config.RpcClientConfiguration;
import dto.Request;
import dto.Response;
import registry.ServiceDiscovery;
import registry.zookeeper.ZookeeperServiceDiscovery;
import serialize.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @description: the stub of client
 * @author: Stroke
 * @date: 2021/04/21
 */
public class ClientStub {

    public static Object getStub(Class clazz, Serializer serializer) {
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // discover the rpc server ip
                ServiceDiscovery discovery = new ZookeeperServiceDiscovery("127.0.0.1:2181");
                InetSocketAddress address = discovery.discoverService(clazz.getName());
                Socket socket = new Socket(address.getAddress(), address.getPort());

                // generate request and send to rpc server
                Request request = new Request();
                //request.setRequestId("1");
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParametersType(method.getParameterTypes());
                request.setParametersValue(args);
                serializer.serialize(request, socket.getOutputStream());

                // get response from rpc server
                Response response = serializer.deserialize(socket.getInputStream(), Response.class);
                socket.close();
                if (response.getError() == null) {
                    return response.getResult();
                } else {
                    return null;
                }
            }
        };

        Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
        return object;
    }

    public static Object getStub(Class clazz){
        Serializer serializer = RpcClientConfiguration.getSerializer();
        return getStub(clazz, serializer);
    }
}
