package constants;

import serialize.factory.ByteArraySerializerFactory;
import serialize.factory.IOStreamSerializerFactory;
import serialize.factory.SerializationTypeEnum;
import serialize.serializer.bytearray.ByteArraySerializer;
import serialize.serializer.iostream.IOStreamSerializer;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public final class StubConstants {

    public static final IOStreamSerializer SOCKET_STUB_DEFAULT_SERIALIZER =
            IOStreamSerializerFactory.getSerializer(SerializationTypeEnum.JDK);
    public static final ByteArraySerializer NETTY_STUB_DEFAULT_SERIALIZER =
            ByteArraySerializerFactory.getSerializer(SerializationTypeEnum.KRYO);
}
