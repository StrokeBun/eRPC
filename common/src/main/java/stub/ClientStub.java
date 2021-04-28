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

    public static Object getInstance(Class clazz, Serializer serializer, ServiceDiscovery discovery) {

        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // if the method is from Object, invoke directly
                try {
                    if (Object.class.equals(method.getDeclaringClass())) {
                        return method.invoke(this, args);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                
                // discover the rpc server socket address
                InetSocketAddress address = discovery.discoverService(clazz.getName());
                Socket socket = new Socket(address.getAddress(), address.getPort());

                // generate request and send it to rpc server
                Request request = new Request();
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParametersType(method.getParameterTypes());
                request.setParametersValue(args);
                serializer.serialize(request, socket.getOutputStream());

                // get response from rpc server
                Response response = serializer.deserialize(socket.getInputStream(), Response.class);
                socket.close();
                return response.getError() == null? response.getResult():null;
            }
        };

        Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, h);
        return object;
    }

    public static Object getInstance(Class clazz, Serializer serializer) {
        final ServiceDiscovery discovery = RpcClientConfiguration.getDefaultServiceDiscovery();
        return getInstance(clazz, serializer, discovery);
    }

    public static Object getInstance(Class clazz){
        final Serializer defaultSerializer = RpcClientConfiguration.getDefaultSerializer();
        return getInstance(clazz, defaultSerializer);
    }

}
