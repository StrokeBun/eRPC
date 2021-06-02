package constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcMessageTypeEnum {

    REQUEST((byte) 0x01),
    RESPONSE((byte) 0x02),
    HEART_BEAT_REQUEST((byte) 0x03),
    HEART_BEAT_RESPONSE((byte) 0x04);

    private byte code;
}
