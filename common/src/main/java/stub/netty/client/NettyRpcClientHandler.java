package stub.netty.client;

import dto.Response;
import dto.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/27
 */
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            Response response = (Response) ((RpcMessage) msg).getData();
            UnprocessedRequestContext.complete(response);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

}
