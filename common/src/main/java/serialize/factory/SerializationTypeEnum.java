package serialize.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
@Getter
@AllArgsConstructor
public enum SerializationTypeEnum {
    KRYO((byte) 0x01, "kryo"),
    PROTOSTUFF((byte) 0x02, "protostuff"),
    JDK((byte) 0x03, "jdk"),
    HESSIAN((byte) 0x04, "hessian");

    private final byte code;
    private final String name;

    public static SerializationTypeEnum getType(byte code) {
        for (SerializationTypeEnum type : SerializationTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
