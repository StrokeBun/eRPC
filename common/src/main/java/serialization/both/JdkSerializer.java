package serialization.both;


import exception.SerializationException;
import serialization.netty.NettySerializer;
import serialization.socket.SocketSerializer;
import java.io.*;

/**
 * @description: Serializer based on jdk
 * @author: Stroke
 * @date: 2021/04/22
 */
public class JdkSerializer implements SocketSerializer, NettySerializer {

    @Override
    public void serialize(Object obj, OutputStream os) throws SerializationException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            throw new SerializationException("Serialization failed" + e.getCause());
        }
    }

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(obj);
            oos.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Serialization failed" + e.getCause());
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializationException {
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Serialization failed" + e.getCause());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(in);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Serialization failed" + e.getCause());
        }
    }
}
