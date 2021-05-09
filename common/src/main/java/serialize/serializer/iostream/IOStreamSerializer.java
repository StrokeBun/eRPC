package serialize.serializer.iostream;

import exception.SerializeException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description: interface of serializer based on io stream
 * @author: Stroke
 * @date: 2021/05/09
 */
public interface IOStreamSerializer {
    /**
     * serialize object and write it using OutputStream
     * @param obj the object to serialize
     * @param os
     * @throws SerializeException
     */
    void serialize(Object obj, OutputStream os) throws SerializeException;

    /**
     * deserialize from InputStream
     * @param is
     * @param clazz
     * @param <T>
     * @return
     * @throws SerializeException
     */
    <T> T deserialize(InputStream is, Class<T> clazz) throws SerializeException;
}
