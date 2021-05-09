package serialize.factory;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import serialize.serializer.both.KryoSerializer;
import serialize.serializer.bytearray.ByteArraySerializer;
import serialize.serializer.bytearray.ProtostuffSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Singleton factory of byte array serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class SingletonByteArraySerializerFactory {

    private static Map<String, ByteArraySerializer> SERIALIZER_MAP = new ConcurrentHashMap<String, ByteArraySerializer>() {
        {
            put("kryo", new KryoSerializer());
            put("protostuff", new ProtostuffSerializer());
        }
    };

    public static ByteArraySerializer getSerializer(String type) {
        final ByteArraySerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZE_TYPE);
        }
        return serializer;
    }

    private SingletonByteArraySerializerFactory() {

    }
}
