package serialize.factory;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import serialize.serializer.bytearray.ByteArraySerializer;
import serialize.serializer.bytearray.ProtostuffSerializer;
import serialize.serializer.iostream.HessianSerializer;
import serialize.serializer.both.KryoSerializer;
import serialize.serializer.iostream.IOStreamSerializer;
import serialize.serializer.iostream.JdkSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Singleton factory of io stream serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class IOStreamSerializerFactory {
    private static Map<SerializationTypeEnum, IOStreamSerializer> SERIALIZER_MAP = new ConcurrentHashMap<SerializationTypeEnum, IOStreamSerializer>() {
        {
            put(SerializationTypeEnum.KRYO, new KryoSerializer());
            put(SerializationTypeEnum.HESSIAN, new HessianSerializer());
            put(SerializationTypeEnum.JDK, new JdkSerializer());
        }
    };

    public static IOStreamSerializer getSerializer(SerializationTypeEnum type) {
        final IOStreamSerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZE_TYPE);
        }
        return serializer;
    }

    private IOStreamSerializerFactory() {

    }
}
