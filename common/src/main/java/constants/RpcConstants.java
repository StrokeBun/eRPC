package constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public final class RpcConstants {

    public static final byte[] MAGIC_NUMBER = {(byte) 'e', (byte) 'r', (byte) 'p', (byte) 'c'};
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final byte VERSION = 1;
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    public static final int HEAD_LENGTH = 12;
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}
