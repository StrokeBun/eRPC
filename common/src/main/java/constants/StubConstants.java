package constants;

import factory.singleton.serialization.SocketSerializerFactory;
import constants.enums.SerializationTypeEnum;
import serialization.socket.SocketSerializer;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public final class StubConstants {

    public static final SocketSerializer SOCKET_STUB_DEFAULT_SERIALIZER =
            SocketSerializerFactory.getInstance(SerializationTypeEnum.JDK);
    public static final SerializationTypeEnum NETTY_STUB_DEFAULT_SERIALIZATION_TYPE =
            SerializationTypeEnum.JDK;
}
