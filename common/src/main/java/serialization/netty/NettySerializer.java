package serialization.netty;

import exception.SerializationException;

/**
 * @description: interface of serializer used in socket based on netty
 * @author: Stroke
 * @date: 2021/05/09
 */
public interface NettySerializer {

    byte[] serialize(Object obj) throws SerializationException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException;
}
