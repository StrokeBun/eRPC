package serialize;

/**
 * @description: Factory of serializer.
 * @author: Stroke
 * @date: 2021/04/22
 */
public final class SerializerFactory {

    public static Serializer getSerializer(String type) {
        switch (type) {
            case "jdk": {
                return new JdkSerializer();
            }
            case "hessian": {
                return new HessianSerializer();
            }
            case "kryo": {
                return new KryoSerializer();
            }
            case "protostuff": {
                return new ProtostuffSerializer();
            }
            default:
                return null;
        }
    }

    private SerializerFactory() {

    }
}
