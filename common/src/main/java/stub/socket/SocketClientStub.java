package stub.socket;

import config.RpcClientConfiguration;
import dto.Request;
import dto.Response;
import exception.enums.RpcErrorMessageEnum;
import exception.RpcException;
import registry.ServiceDiscovery;
import serialize.serializer.iostream.IOStreamSerializer;

import java.io.IOException;
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
public class SocketClientStub {
    private static final String INTERFACE_NAME = "interfaceName";
    private RpcClientConfiguration configuration;

    public SocketClientStub() {
        configuration = RpcClientConfiguration.builder().build();
    }

    public SocketClientStub(RpcClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public<T> T getInstance(Class<T> clazz) {
        class ClientHandler implements InvocationHandler {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws IOException {
                // if the method is from Object, invoke directly
                try {
                    if (Object.class.equals(method.getDeclaringClass())) {
                        return method.invoke(this, args);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                String className = clazz.getSimpleName();
                // discover the rpc server socket address
                final ServiceDiscovery discovery = configuration.getServiceDiscovery();
                InetSocketAddress address = discovery.discoverService(className);
                try (Socket socket = new Socket(address.getAddress(), address.getPort())) {
                    // generate request and send it to rpc server
                    Request request = Request.builder()
                            .requestId(UUID.randomUUID().toString())
                            .className(className)
                            .methodName(method.getName())
                            .parametersType(method.getParameterTypes())
                            .parametersValue(args)
                            .build();
                    final IOStreamSerializer serializer = configuration.getSerializer();
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
        return (T)object;
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
