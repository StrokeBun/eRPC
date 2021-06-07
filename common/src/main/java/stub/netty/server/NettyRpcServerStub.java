package stub.netty.server;

import config.RpcServerConfiguration;
import constants.StubConstants;
import constants.enums.CompressionEnum;
import constants.enums.SerializationEnum;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import stub.BaseServerStub;
import stub.netty.codec.RpcMessageDecoder;
import stub.netty.codec.RpcMessageEncoder;
import stub.netty.server.handler.NettyRpcServerIdleCheckHandler;
import stub.netty.server.handler.NettyRpcServerMessageHandler;

import java.net.InetSocketAddress;

/**
 * @description: Server stub based on netty.
 * @author: Stroke
 * @date: 2021/05/10
 */
public class NettyRpcServerStub extends BaseServerStub {
    @Getter
    private SerializationEnum serializationType = StubConstants.DEFAULT_SERIALIZATION_TYPE;
    @Getter
    private CompressionEnum compressionType = StubConstants.DEFAULT_COMPRESSION_TYPE;

    public NettyRpcServerStub() {
        super();
    }

    public NettyRpcServerStub(RpcServerConfiguration configuration) {
        super(configuration);
    }

    public NettyRpcServerStub(SerializationEnum serializationType) {
        super();
        this.serializationType = serializationType;
    }

    public NettyRpcServerStub(RpcServerConfiguration configuration, SerializationEnum serializationType) {
        super(configuration);
        this.serializationType = serializationType;
    }


    @Override
    public void run() throws Exception {
        final NettyRpcServerStub serverStub = this;
        EventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boos"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        // TODO: add handler thread pool.
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // use Nagle
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .localAddress(new InetSocketAddress(configuration.getServerPort()))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast("serverIdleCheckHandler", new NettyRpcServerIdleCheckHandler())
                                    .addLast("rpcMessageEncoder", new RpcMessageEncoder())
                                    .addLast("rpcMessageDecoder", new RpcMessageDecoder())
                                    .addLast("serverMessageHandler", new NettyRpcServerMessageHandler(serverStub))
                                    .addLast("serverLoggingHandler", new LoggingHandler(LogLevel.INFO));
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
            removeService();
        }

    }

}
