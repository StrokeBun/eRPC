package constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
