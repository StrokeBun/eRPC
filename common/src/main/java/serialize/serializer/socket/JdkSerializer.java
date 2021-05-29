package serialize.serializer.socket;


import exception.SerializeException;

import java.io.*;

/**
 * @description: Serializer based on jdk
 * @author: Stroke
 * @date: 2021/04/22
 */
public class JdkSerializer implements SocketSerializer {
    @Override
    public void serialize(Object obj, OutputStream os) throws SerializeException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            throw new SerializeException("Serialization failed" + e.getCause());
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializeException{
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializeException("Serialization failed" + e.getCause());
        }
    }
}
