package constants;

import serialize.factory.NettySerializerFactory;
import serialize.factory.SocketSerializerFactory;
import serialize.factory.SerializationTypeEnum;
import serialize.serializer.netty.NettySerializer;
import serialize.serializer.socket.SocketSerializer;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public final class StubConstants {

    public static final SocketSerializer SOCKET_STUB_DEFAULT_SERIALIZER =
            SocketSerializerFactory.getInstance(SerializationTypeEnum.JDK);
    public static final SerializationTypeEnum NETTY_STUB_DEFAULT_SERIALIZATION_TYPE =
            SerializationTypeEnum.PROTOSTUFF;
}
