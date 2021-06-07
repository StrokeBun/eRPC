package stub.netty.client.handler;

import constants.enums.RpcMessageTypeEnum;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import stub.netty.client.UnprocessedRequestContext;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/27
 */
@Slf4j
public class NettyRpcClientMessageHandler extends SimpleChannelInboundHandler<RpcMessage> {

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

}
