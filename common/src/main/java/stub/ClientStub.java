package stub;

/**
 * @description: RPC client stub.
 * @author: Stroke
 * @date: 2021/05/12
 */
public interface ClientStub {
   <T> T getInstance(Class<T> clazz);
}
