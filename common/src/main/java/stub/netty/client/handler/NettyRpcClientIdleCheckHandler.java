package stub.netty.client.handler;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/06/07
 */
public class NettyRpcClientIdleCheckHandler extends IdleStateHandler {

    public NettyRpcClientIdleCheckHandler() {
        super(0, 5, 0);
    }

}
