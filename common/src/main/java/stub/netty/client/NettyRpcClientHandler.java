package stub.netty.client;

import constants.RpcMessageBodyConstants;
import constants.enums.RpcMessageTypeEnum;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/27
 */
@Slf4j
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private NettyRpcClientStub clientStub;

    public NettyRpcClientHandler(NettyRpcClientStub clientStub) {
        this.clientStub = clientStub;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage rpcMessage) throws Exception {
        byte messageType = rpcMessage.getMessageType();
        if (RpcMessageTypeEnum.HEART_BEAT_RESPONSE.getCode() == messageType) {
            log.info("receive heart beat response from " + ctx.channel().remoteAddress());
        } else if (RpcMessageTypeEnum.RESPONSE.getCode() == messageType) {
            Response response = (Response) rpcMessage.getData();
            UnprocessedRequestContext.complete(response);
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
}
