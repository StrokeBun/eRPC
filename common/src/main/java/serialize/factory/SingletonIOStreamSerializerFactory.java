package serialize.factory;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
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
public final class SingletonIOStreamSerializerFactory {

    private static Map<String, IOStreamSerializer> SERIALIZER_MAP = new ConcurrentHashMap<String, IOStreamSerializer>() {
        {
            put("jdk", new JdkSerializer());
            put("hessian", new HessianSerializer());
            put("kryo", new KryoSerializer());
        }
    };

    public static IOStreamSerializer getSerializer(String type) {
        final IOStreamSerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZE_TYPE);
        }
        return serializer;
    }

    private SingletonIOStreamSerializerFactory() {

    }
}
