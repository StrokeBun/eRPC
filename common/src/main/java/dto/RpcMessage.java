package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: Rpc message transported in network.
 * @author: Stroke
 * @date: 2021/05/25
 */
@Getter
@Setter
@Builder
public class RpcMessage {

    /**
     * messageType: one byte to show the message type, defined in RpcMessageTypeEnum.
     * @see constants.enums.RpcMessageTypeEnum
     */
    private byte messageType;
    /**
     * serializationCode: one byte to show the serialization used in this message, defined in CompressionEnum.
     * @see constants.enums.CompressionEnum
     */
    private byte serializationCode;
    /**
     * compressionCode: one byte to show the compression used in this message, defined in SerializationEnum.
     * @see constants.enums.SerializationEnum
     */
    private byte compressionCode;
    private Object data;

}
