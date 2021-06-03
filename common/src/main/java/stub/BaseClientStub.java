package stub;

import config.RpcClientConfiguration;
import dto.Request;
import dto.Response;
import exception.RpcException;
import exception.enums.RpcErrorMessageEnum;
import registry.ServiceDiscovery;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @description: A skeletal implementation of the {@link ClientStub}.
 * @author: Stroke
 * @date: 2021/05/12
 */
public abstract class BaseClientStub implements ClientStub {
    private static final String INTERFACE_NAME = "interfaceName";
    private RpcClientConfiguration configuration;

    public BaseClientStub() {
        configuration = RpcClientConfiguration.builder().build();
    }

    public BaseClientStub(RpcClientConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public<T> T getInstance(Class<T> clazz) {
        class ClientHandler implements InvocationHandler {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws IOException, ExecutionException, InterruptedException {
                // if the method is from Object, invoke directly
                try {
                    if (Object.class.equals(method.getDeclaringClass())) {
                        return method.invoke(this, args);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                // discover the rpc server socket address
                String className = clazz.getSimpleName();
                final ServiceDiscovery discovery = configuration.getServiceDiscovery();
                InetSocketAddress address = discovery.discoverService(className);

                // generate request
                Request request = new Request();
                request.setRequestId(UUID.randomUUID().toString());
                request.setClassName(className);
                request.setMethodName(method.getName());
                request.setParametersType(method.getParameterTypes());
                request.setParametersValue(args);

                Response response = sendRequest(request, address, configuration);

                // check the response
                check(request, response);
                return response.getResult();
            }
        }
        Object object = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ClientHandler());
        return clazz.cast(object);
    }

    public abstract Response sendRequest(Request request, InetSocketAddress address, RpcClientConfiguration configuration) throws IOException, ExecutionException, InterruptedException;

    private void check(Request request, Response response) {
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
