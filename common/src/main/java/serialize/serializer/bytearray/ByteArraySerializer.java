package serialize.serializer.bytearray;

import exception.SerializeException;

/**
 * @description: interface of serializer based on byte array
 * @author: Stroke
 * @date: 2021/05/09
 */
public interface ByteArraySerializer {

    byte[] serialize(Object obj) throws SerializeException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializeException;
}
