package factory.singleton.serialization;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import constants.enums.SerializationTypeEnum;
import serialization.both.KryoSerializer;
import serialization.netty.NettySerializer;
import serialization.netty.ProtostuffSerializer;
import serialization.socket.JdkSerializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Singleton factory of byte array serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class NettySerializerFactory {

    private static Map<SerializationTypeEnum, NettySerializer> SERIALIZER_MAP = new ConcurrentHashMap<SerializationTypeEnum, NettySerializer>() {
        {
            put(SerializationTypeEnum.KRYO, new KryoSerializer());
            put(SerializationTypeEnum.PROTOSTUFF, new ProtostuffSerializer());
            put(SerializationTypeEnum.JDK, new JdkSerializer());
        }
    };

    public static NettySerializer getInstance(SerializationTypeEnum type) {
        final NettySerializer serializer = SERIALIZER_MAP.get(type);
        if (serializer == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_SERIALIZE_TYPE);
        }
        return serializer;
    }

    private NettySerializerFactory() {

    }
}
