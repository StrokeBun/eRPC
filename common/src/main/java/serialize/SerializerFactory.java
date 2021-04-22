package serialize;

/**
 * @description:
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
            default:
                return null;
        }
    }

    private SerializerFactory() {

    }
}
