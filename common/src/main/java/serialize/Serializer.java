package serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description: interface of Serializer, used to serialize and deserialize
 * @author: Stroke
 * @date: 2021/04/22
 */
public interface Serializer {

    public void serialize(Object obj, OutputStream os) throws IOException;

    public <T> T deserialize(InputStream is, Class<T> clazz) throws IOException, ClassNotFoundException;
}
