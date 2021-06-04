package serialization.both;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dto.Request;
import dto.Response;
import exception.DeserializationException;
import exception.SerializationException;
import serialization.netty.NettySerializer;
import serialization.socket.SocketSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @description: Serializer based on kryo.
 * @author: Stroke
 * @date: 2021/04/22
 */
public class KryoSerializer implements SocketSerializer, NettySerializer {

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
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException(this.getClass().getSimpleName() + " " + e.getCause());
        }
    }

    @Override
    public <T> T deserialize(InputStream is, Class<T> clazz) {
        Kryo kryo = kryoThreadLocal.get();
        Input input = new Input(is);
        T result = kryo.readObject(input, clazz);
        kryoThreadLocal.remove();
        return result;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object result = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return (T) result;
        } catch (Exception e) {
            throw new DeserializationException(this.getClass().getSimpleName() + " " + e.getCause());
        }
    }
}
