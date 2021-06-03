package constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: Enums of compression type.
 * @author: Stroke
 * @date: 2021/06/01
 */
@Getter
@AllArgsConstructor
public enum CompressionEnum {

    ZIP((byte)0x01, "zip"),
    GZIP((byte)0x02, "gzip"),
    BZIP2((byte)0x03, "bzip2");

    private final byte code;
    private final String name;

    public static CompressionEnum getType(byte code) {
        for (CompressionEnum type : CompressionEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
