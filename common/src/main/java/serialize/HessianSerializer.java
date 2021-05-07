package serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.*;

/**
 * @description: Serializer based on Hessian
 * @author: Stroke
 * @date: 2021/04/22
 */
public class HessianSerializer implements Serializer {

    @Override
    public void serialize(Object obj, OutputStream os) throws IOException {
        Hessian2Output output = new Hessian2Output(os);
        output.writeObject(obj);
        output.flush();
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) throws IOException {
        Hessian2Input input = new Hessian2Input(is);
        return (T)input.readObject();
    }

}
