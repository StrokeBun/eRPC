package serialization.socket;

import exception.SerializationException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description: Serializer used in {@link stub.socket.SocketClientStub} and {@link stub.socket.SocketServerStub}.
 * @author: Stroke
 * @date: 2021/05/09
 */
public interface SocketSerializer {
    /**
     * serialize object and write it using OutputStream
     * @param obj the object to serialize
     * @param os
     * @throws SerializationException
     */
    void serialize(Object obj, OutputStream os) throws SerializationException;

    /**
     * deserialize from InputStream
     * @param is
     * @param clazz
     * @param <T>
     * @return
     * @throws SerializationException
     */
    <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException;
}
