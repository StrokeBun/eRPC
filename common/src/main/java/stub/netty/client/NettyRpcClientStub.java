package stub.netty.client;

import config.RpcClientConfiguration;
import constants.StubConstants;
import constants.enums.CompressionEnum;
import constants.enums.RpcMessageTypeEnum;
import constants.enums.SerializationEnum;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import stub.BaseClientStub;
import stub.netty.codec.RpcMessageDecoder;
import stub.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @description: Client stub based on netty.
 * @author: Stroke
 * @date: 2021/05/10
 */
public class NettyRpcClientStub extends BaseClientStub {

    private Bootstrap bootstrap;
    private ChannelProvider channelProvider;
    private SerializationEnum serializationType = StubConstants.DEFAULT_SERIALIZATION_TYPE;
    private CompressionEnum compressionType = StubConstants.DEFAULT_COMPRESSION_TYPE;

    public NettyRpcClientStub() {
        init();
    }

    public NettyRpcClientStub(RpcClientConfiguration configuration) {
        super(configuration);
        init();
    }

    private void init() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final NettyRpcClientHandler handler = new NettyRpcClientHandler(this);
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new RpcMessageEncoder())
                                .addLast(new RpcMessageDecoder())
                                .addLast(handler)
                                .addLast(new LoggingHandler(LogLevel.INFO));
                    }
                });
        this.channelProvider = new ChannelProvider();
    }

    @Override
    public Response sendRequest(Request request, InetSocketAddress address, RpcClientConfiguration configuration) throws ExecutionException, InterruptedException {
        Channel channel = getChannel(address);
        CompletableFuture<Response> resultFuture = new CompletableFuture<>();
        if (channel.isActive()) {
            // send request async
            UnprocessedRequestContext.put(request.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder()
                    .data(request)
                    .serializationCode(serializationType.getCode())
                    .compressionCode(compressionType.getCode())
                    .messageType(RpcMessageTypeEnum.REQUEST.getCode())
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

    public Channel getChannel(InetSocketAddress address) throws ExecutionException, InterruptedException {
        Channel channel = channelProvider.get(address);
        if (channel == null) {
            channel = doConnect(address);
            channelProvider.set(address, channel);
        }
        return channel;
    }

    private Channel doConnect(InetSocketAddress address) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(address).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public SerializationEnum getSerializationType() {
        return serializationType;
    }

    public CompressionEnum getCompressionType() {
        return compressionType;
    }
}
