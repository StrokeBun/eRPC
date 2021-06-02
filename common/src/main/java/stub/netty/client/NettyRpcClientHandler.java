package stub.netty.client;

import constants.RpcMessageBodyConstants;
import constants.enums.RpcMessageTypeEnum;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/27
 */
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private NettyClientStub clientStub;

    public NettyRpcClientHandler(NettyClientStub clientStub) {
        this.clientStub = clientStub;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                byte messageType = rpcMessage.getMessageType();
                if (RpcMessageTypeEnum.HEART_BEAT_RESPONSE.getCode() == messageType) {
                    System.out.println("heart beat response");
                } else if (RpcMessageTypeEnum.RESPONSE.getCode() == messageType) {
                    Response response = (Response) rpcMessage.getData();
                    UnprocessedRequestContext.complete(response);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    // heart beat
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                Channel channel = clientStub.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = RpcMessage.builder()
                        .messageType(RpcMessageTypeEnum.HEART_BEAT_REQUEST.getCode())
                        .compressionCode(clientStub.getCompressionType().getCode())
                        .serializationCode(clientStub.getSerializationType().getCode())
                        .data(RpcMessageBodyConstants.PING)
                        .build();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
