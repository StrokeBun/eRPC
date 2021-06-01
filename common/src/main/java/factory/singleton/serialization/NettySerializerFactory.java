package factory.singleton.serialization;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import constants.enums.SerializationEnum;
import serialization.both.KryoSerializer;
import serialization.netty.NettySerializer;
import serialization.netty.ProtostuffSerializer;
import serialization.both.JdkSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Singleton factory of byte array serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class NettySerializerFactory {

    private static Map<SerializationEnum, NettySerializer> SERIALIZER_MAP = new ConcurrentHashMap<SerializationEnum, NettySerializer>() {
        {
            put(SerializationEnum.KRYO, new KryoSerializer());
            put(SerializationEnum.PROTOSTUFF, new ProtostuffSerializer());
            put(SerializationEnum.JDK, new JdkSerializer());
        }
    };

    public static NettySerializer getInstance(SerializationEnum type) {
        final NettySerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZATION_TYPE);
        }
        return serializer;
    }

    private NettySerializerFactory() {

    }
}
