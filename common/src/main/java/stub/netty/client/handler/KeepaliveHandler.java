package stub.netty.client.handler;

import constants.RpcMessageBodyConstants;
import constants.enums.RpcMessageTypeEnum;
import dto.RpcMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import stub.netty.client.NettyRpcClientStub;

import java.net.InetSocketAddress;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/06/07
 */
@Slf4j
public class KeepaliveHandler extends ChannelDuplexHandler {

    private NettyRpcClientStub clientStub;

    public KeepaliveHandler(NettyRpcClientStub clientStub) {
        this.clientStub = clientStub;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            log.info("write idle happen, so need to send keepalive message to keep connection");
            Channel channel = clientStub.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
            RpcMessage rpcMessage = RpcMessage.builder()
                    .messageType(RpcMessageTypeEnum.HEART_BEAT_REQUEST.getCode())
                    .compressionCode(clientStub.getCompressionType().getCode())
                    .serializationCode(clientStub.getSerializationType().getCode())
                    .data(RpcMessageBodyConstants.PING)
                    .build();
            channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
    }
}
