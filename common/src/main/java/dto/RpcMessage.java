package dto;

import lombok.Getter;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
@Getter
public class RpcMessage {

    private byte messageType;
    private byte serializationType;
    // request data
    private Object data;
}
