package exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigurationErrorMessageEnum {
    MISS_CLIENT_CONFIGURATION_FILE("miss configuration file rpc-client-config.properties"),
    MISS_SERVER_CONFIGURATION_FILE("miss configuration file rpc-server-config.properties"),
    MISS_REGISTRY_SERVER_TYPE("miss registry server type"),
    MISS_REGISTRY_SERVER_ADDRESS("miss registry server address"),
    WRONG_PORT_FORMAT("wrong port format");

    private final String message;
}
