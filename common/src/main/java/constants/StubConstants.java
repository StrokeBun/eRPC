package constants;

import constants.enums.CompressionEnum;
import factory.singleton.serialization.SocketSerializerFactory;
import constants.enums.SerializationEnum;
import serialization.socket.SocketSerializer;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public final class StubConstants {

    public static final SocketSerializer SOCKET_STUB_DEFAULT_SERIALIZER =
            SocketSerializerFactory.getInstance(SerializationEnum.JDK);
    public static final SerializationEnum DEFAULT_SERIALIZATION_TYPE =
            SerializationEnum.JDK;
    public static final CompressionEnum DEFAULT_COMPRESSION_TYPE =
            CompressionEnum.BZIP2;
}
