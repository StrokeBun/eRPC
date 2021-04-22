package serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dto.Request;
import dto.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/22
 */
public class KryoSerializer implements Serializer {

    /**
     * Because Kryo is not thread safe. So, use ThreadLocal to store Kryo objects
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(Request.class);
        kryo.register(Response.class);
        return kryo;
    });

    @Override
    public void serialize(Object obj, OutputStream os) {
        Kryo kryo = kryoThreadLocal.get();
        Output output = new Output(os);
        kryo.writeObject(output, obj);
        output.flush();
        kryoThreadLocal.remove();
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        Kryo kryo = kryoThreadLocal.get();
        Input input = new Input(is);
        T result = kryo.readObject(input, clazz);
        kryoThreadLocal.remove();
        return result;
    }
}
