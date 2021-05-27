package stub.netty.client;

import config.RpcClientConfiguration;
import constants.RpcConstants;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import serialize.factory.SerializationTypeEnum;
import stub.BaseClientStub;
import stub.netty.codec.RpcMessageDecoder;
import stub.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @description: client stub based on netty
 * @author: Stroke
 * @date: 2021/05/10
 */
public class NettyClientStub extends BaseClientStub {

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;
    private SerializationTypeEnum serializationType = SerializationTypeEnum.KRYO;

    public NettyClientStub() {
        init();
    }

    public NettyClientStub(RpcClientConfiguration configuration) {
        super(configuration);
        init();
    }

    private void init() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new RpcMessageEncoder())
                                .addLast(new RpcMessageDecoder())
                                .addLast(new ClientHandler());
                    }
                });
    }

    @Override
    public Response sendRequest(Request request, InetSocketAddress address, RpcClientConfiguration configuration) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(address).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        Channel channel =  completableFuture.get();

        CompletableFuture<Response> resultFuture = new CompletableFuture<>();
        if (channel.isActive()) {
            // put unprocessed request
            UnprocessedRequestUtils.put(request.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder()
                    .data(request)
                    .serializationType(serializationType.getCode())
                    .compress((byte) 1)
                    .messageType(RpcConstants.REQUEST_TYPE)
                    .build();

            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture.get();
    }

}
