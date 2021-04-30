package serialize;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Singleton factory of serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class SingletonSerializerFactory {

    private static Map<String, Serializer> SERIALIZER_MAP = new ConcurrentHashMap<String, Serializer>() {
        {
            put("jdk", new JdkSerializer());
            put("hessian", new HessianSerializer());
            put("kryo", new KryoSerializer());
        }
    };

    public static Serializer getSerializer(String type) {
        final Serializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZE_TYPE);
        }
        return serializer;
    }

    private SingletonSerializerFactory() {

    }
}
