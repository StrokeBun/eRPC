package stub.netty.server;

import constants.RpcConstants;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import serialize.factory.SerializationTypeEnum;
import stub.ServerStubUtils;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private NettyServerStub serverStub;

    public ServerHandler(NettyServerStub serverStub) {
        this.serverStub = serverStub;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            Request request = (Request) ((RpcMessage) msg).getData();
            String className = request.getClassName();
            request.setClassName(serverStub.getRegisterTable().get(className));
            Response response = ServerStubUtils.getResponse(request);

            RpcMessage rpcMessage = RpcMessage.builder()
                    .serializationType(serverStub.getSerializationType().getCode())
                    .compress((byte) 1)
                    .messageType(RpcConstants.RESPONSE_TYPE)
                    .data(response)
                    .build();
            ctx.writeAndFlush(rpcMessage);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
