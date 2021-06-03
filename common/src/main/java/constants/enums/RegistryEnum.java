package constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: Enums of registry server type.
 * @author: Stroke
 * @date: 2021/06/03
 */
@Getter
@AllArgsConstructor
public enum RegistryEnum {

    ZOOKEEPER("zookeeper"),
    REDIS("redis");

    private String name;

    public static RegistryEnum match(String name) {
        for (RegistryEnum typeEnum : RegistryEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
