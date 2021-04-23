package serialize;


import java.io.*;

/**
 * @description: Serializer based on jdk
 * @author: Stroke
 * @date: 2021/04/22
 */
public class JdkSerializer implements Serializer {
    @Override
    public void serialize(Object obj, OutputStream os) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(obj);
        oos.flush();
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(is);
        return (T) ois.readObject();
    }
}
