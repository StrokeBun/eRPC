package stub.netty.server;

import config.RpcServerConfiguration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import stub.BaseServerStub;
import stub.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;

/**
 * @description: server stub based on netty
 * @author: Stroke
 * @date: 2021/05/10
 */
public class NettyServerStub extends BaseServerStub {

    public NettyServerStub() {
        super();
    }

    public NettyServerStub(RpcServerConfiguration configuration) {
        super(configuration);
    }

    @Override
    public void run() throws Exception {

        final ServerHandler handler = new ServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(configuration.getServerPort()))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new RpcMessageEncoder())
                                    .addLast(handler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
            removeService();
        }

    }
}
