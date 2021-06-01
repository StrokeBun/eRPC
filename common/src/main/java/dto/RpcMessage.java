package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 *
 * serializationCode: one byte to show the serialization used in this message, defined in CompressionEnum.
 * @see constants.enums.CompressionEnum
 * compressionCode: one byte to show the compression used in this message, defined in SerializationEnum.
 * @see constants.enums.SerializationEnum
 *
 * @author: Stroke
 * @date: 2021/05/25
 */
@Getter
@Setter
@Builder
public class RpcMessage {

    private byte messageType;
    private byte serializationCode;
    private byte compressionCode;
    // request data
    private Object data;

}
