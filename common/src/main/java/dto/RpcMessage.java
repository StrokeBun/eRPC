package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
@Getter
@Setter
@Builder
public class RpcMessage {

    private byte messageType;
    private byte serializationType;
    private byte compressionType;
    // request data
    private Object data;

}
