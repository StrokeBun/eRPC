package stub.netty.server;

import constants.RpcConstants;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private NettyRpcServerStub serverStub;

    public NettyServerHandler(NettyRpcServerStub serverStub) {
        this.serverStub = serverStub;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // invoke the method
            RpcMessage message = (RpcMessage) msg;
            Request request = (Request) message.getData();
            String className = request.getClassName();
            request.setClassName(serverStub.getRegisterTable().get(className));
            Response response = serverStub.getResponse(request);
            // generate rpc message
            RpcMessage rpcMessage = RpcMessage.builder()
                    .serializationCode(serverStub.getSerializationType().getCode())
                    .compressionCode(serverStub.getCompressionType().getCode())
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
