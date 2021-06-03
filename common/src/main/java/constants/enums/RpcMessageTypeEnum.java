package constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: Enums of rpc message type transported in network.
 * @author: Stroke
 * @date: 2021/06/03
 */
@AllArgsConstructor
@Getter
public enum RpcMessageTypeEnum {

    REQUEST((byte) 0x01),
    RESPONSE((byte) 0x02),
    HEART_BEAT_REQUEST((byte) 0x03),
    HEART_BEAT_RESPONSE((byte) 0x04);

    private byte code;
}
