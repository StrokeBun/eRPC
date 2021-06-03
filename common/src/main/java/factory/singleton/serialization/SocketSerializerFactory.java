package factory.singleton.serialization;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import constants.enums.SerializationEnum;
import serialization.socket.HessianSerializer;
import serialization.both.KryoSerializer;
import serialization.socket.SocketSerializer;
import serialization.both.JdkSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: A singleton factory to create io stream serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class SocketSerializerFactory {
    private static Map<SerializationEnum, SocketSerializer> SERIALIZER_MAP = new ConcurrentHashMap<SerializationEnum, SocketSerializer>() {
        {
            put(SerializationEnum.KRYO, new KryoSerializer());
            put(SerializationEnum.HESSIAN, new HessianSerializer());
            put(SerializationEnum.JDK, new JdkSerializer());
        }
    };

    public static SocketSerializer getInstance(SerializationEnum type) {
        final SocketSerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZATION_TYPE);
        }
        return serializer;
    }

    private SocketSerializerFactory() {

    }
}
