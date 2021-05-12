package stub.client;

import config.RpcClientConfiguration;
import dto.Request;
import dto.Response;
import serialize.factory.SingletonIOStreamSerializerFactory;
import serialize.serializer.iostream.IOStreamSerializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * @description: client stub based on socket
 * @author: Stroke
 * @date: 2021/04/21
 */
public class SocketClientStub extends BaseClientStub {

    private static IOStreamSerializer DEFAULT_SERIALIZER = SingletonIOStreamSerializerFactory.getSerializer("jdk");
    private IOStreamSerializer serializer = DEFAULT_SERIALIZER;

    public SocketClientStub() {
        super();
    }

    public SocketClientStub(RpcClientConfiguration configuration) {
        super(configuration);
    }

    public SocketClientStub(RpcClientConfiguration configuration, IOStreamSerializer serializer) {
        super(configuration);
        this.serializer = serializer;
    }

    @Override
    public Response sendRequest(Request request, InetSocketAddress address, RpcClientConfiguration configuration) throws IOException {
        try (Socket socket = new Socket(address.getAddress(), address.getPort())) {
            // generate request and send it to rpc server
            serializer.serialize(request, socket.getOutputStream());
            // get response from rpc server
            return serializer.deserialize(socket.getInputStream(), Response.class);
        }
    }

}
