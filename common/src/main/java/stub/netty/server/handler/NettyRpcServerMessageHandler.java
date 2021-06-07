package stub.netty.server.handler;

import constants.RpcMessageBodyConstants;
import constants.enums.RpcMessageTypeEnum;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import stub.netty.server.NettyRpcServerStub;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
@Slf4j
public class NettyRpcServerMessageHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private NettyRpcServerStub serverStub;

    public NettyRpcServerMessageHandler(NettyRpcServerStub serverStub) {
        this.serverStub = serverStub;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcMessage message)  {
        RpcMessage rpcMessage = RpcMessage.builder()
                .serializationCode(serverStub.getSerializationType().getCode())
                .compressionCode(serverStub.getCompressionType().getCode())
                .build();

        byte messageType = message.getMessageType();
        if (RpcMessageTypeEnum.HEART_BEAT_REQUEST.getCode() == messageType) {
            log.info("receive heart beat request from " + ctx.channel().remoteAddress());
            rpcMessage.setMessageType(RpcMessageTypeEnum.HEART_BEAT_RESPONSE.getCode());
            rpcMessage.setData(RpcMessageBodyConstants.PONG);
        } else {
            // invoke the method
            Request request = (Request) message.getData();
            String className = request.getClassName();
            request.setClassName(serverStub.getRegisterTable().get(className));
            Response response = serverStub.getResponse(request);
            rpcMessage.setMessageType(RpcMessageTypeEnum.RESPONSE.getCode());
            rpcMessage.setData(response);
        }
        ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }
}
