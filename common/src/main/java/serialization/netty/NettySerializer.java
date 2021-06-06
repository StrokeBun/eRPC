package serialization.netty;

import exception.SerializationException;
import stub.netty.client.NettyRpcClientStub;

/**
 * @description: Serializer used in {@link NettyRpcClientStub} and {@link stub.netty.server.NettyRpcServerStub}.
 * @author: Stroke
 * @date: 2021/05/09
 */
public interface NettySerializer {

    byte[] serialize(Object obj) throws SerializationException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException;
}
