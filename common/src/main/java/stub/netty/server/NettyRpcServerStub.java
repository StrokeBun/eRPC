package stub.netty.server;

import config.RpcServerConfiguration;

import constants.StubConstants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Builder;
import lombok.Getter;
import serialize.factory.SerializationTypeEnum;
import stub.BaseServerStub;
import stub.netty.client.NettyClientStub;
import stub.netty.codec.RpcMessageDecoder;
import stub.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: server stub based on netty
 * @author: Stroke
 * @date: 2021/05/10
 */
public class NettyRpcServerStub extends BaseServerStub {
    @Getter
    private SerializationTypeEnum serializationType = StubConstants.NETTY_STUB_DEFAULT_SERIALIZATION_TYPE;

    public NettyRpcServerStub() {
        super();
    }

    public NettyRpcServerStub(RpcServerConfiguration configuration) {
        super(configuration);
    }

    public NettyRpcServerStub(SerializationTypeEnum serializationType) {
        super();
        this.serializationType = serializationType;
    }

    public NettyRpcServerStub(RpcServerConfiguration configuration, SerializationTypeEnum serializationType) {
        super(configuration);
        this.serializationType = serializationType;
    }


    @Override
    public void run() throws Exception {

        final NettyServerHandler handler = new NettyServerHandler(this);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(configuration.getServerPort()))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new RpcMessageEncoder())
                                    .addLast(new RpcMessageDecoder())
                                    .addLast(handler);
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
