package stub.netty.client;

import config.RpcClientConfiguration;
import dto.Request;
import dto.Response;
import stub.BaseClientStub;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @description: client stub based on netty
 * @author: Stroke
 * @date: 2021/05/10
 */
public class NettyClientStub extends BaseClientStub {

    @Override
    public Response sendRequest(Request request, InetSocketAddress address, RpcClientConfiguration configuration) throws IOException {
        return null;
    }

}
