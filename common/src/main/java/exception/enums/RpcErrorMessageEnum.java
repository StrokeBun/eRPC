package exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/29
 */
@Getter
@AllArgsConstructor
public enum RpcErrorMessageEnum {
    CLIENT_CONNECT_SERVER_FAILURE("client connect to server failed"),
    SERVICE_INVOCATION_FAILURE("service invocation failed"),
    SERVICE_CANT_BE_FOUND("service not found"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("service not implemented"),
    REQUEST_NOT_MATCH_RESPONSE("response dont match request");

    private final String message;
}
