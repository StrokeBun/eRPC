package stub;

import config.RpcClientConfig;
import dto.Request;
import dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;


/**
 * @description: the stub of client
 * @author: Stroke
 * @date: 2021/04/21
 */
public class ClientStub {

    public static Object getStub(Class clazz){
        InvocationHandler h = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String serverIp = RpcClientConfig.getRpcServerIp();
                int serverPort = RpcClientConfig.getRpcServerPort();
                Socket socket = new Socket(serverIp, serverPort);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Request request = new Request();
                request.setRequestId("1");
                request.setClassName(clazz.getName());
                request.setMethodName(method.getName());
                request.setParametersType(method.getParameterTypes());
                request.setParametersValue(args);

                oos.writeObject(request);
                oos.flush();


                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Response response = (Response) ois.readObject();
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
}
