package serialize.serializer.iostream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import exception.SerializeException;

import java.io.*;

/**
 * @description: Serializer based on Hessian
 * @author: Stroke
 * @date: 2021/04/22
 */
public class HessianSerializer implements IOStreamSerializer {

    @Override
    public void serialize(Object obj, OutputStream os) throws SerializeException {
        Hessian2Output output = new Hessian2Output(os);
        try {
            output.writeObject(obj);
            output.flush();
        } catch (IOException e) {
            throw new SerializeException("Serialization failed" + e.getCause());
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws SerializeException {
        try {
            Hessian2Input input = new Hessian2Input(is);
            return (T)input.readObject();
        } catch (IOException e) {
            throw new SerializeException("Deserialization failed" + e.getCause());
        }
    }

    public static void main(String[] args) {
    }
}
