package stub;

import config.RpcClientConfiguration;
import dto.Request;
import dto.Response;
import exception.enums.RpcErrorMessageEnum;
import exception.RpcException;
import lombok.AllArgsConstructor;
import registry.ServiceDiscovery;
import serialize.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * @description: the stub of client
 * @author: Stroke
 * @date: 2021/04/21
 */
@AllArgsConstructor
public class ClientStub {
    private static final String INTERFACE_NAME = "interfaceName";
    private Serializer serializer;
    private ServiceDiscovery discovery;

    public ClientStub() {
        serializer = RpcClientConfiguration.getDefaultSerializer();
        discovery = RpcClientConfiguration.getDefaultServiceDiscovery();
    }

    public Object getInstance(Class clazz) {
        class ClientHandler implements InvocationHandler {
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
                try (Socket socket = new Socket(address.getAddress(), address.getPort())) {
                    // generate request and send it to rpc server
                    Request request = new Request();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setClassName(clazz.getName());
                    request.setMethodName(method.getName());
                    request.setParametersType(method.getParameterTypes());
                    request.setParametersValue(args);
                    serializer.serialize(request, socket.getOutputStream());

                    // get response from rpc server
                    Response response = serializer.deserialize(socket.getInputStream(), Response.class);
                    // check the response
                    check(request, response);
                    return response.getResult();
                }
            }
        }
        Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ClientHandler());
        return object;
    }


    public void check(Request request, Response response) {
        if (response == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getMethodName());
        }

        if (!request.getRequestId().equals(response.getResponseId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + request.getMethodName());
        }

        if (response.getError() != null) {
            throw new RpcException(response.getError() + ":" + INTERFACE_NAME + ":" + request.getMethodName());
        }
    }

}
