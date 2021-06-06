package stub.netty.codec;

import compression.Compressor;
import constants.RpcMessageBodyConstants;
import constants.RpcMessageHeaderConstants;
import constants.enums.CompressionEnum;
import constants.enums.RpcMessageTypeEnum;
import constants.enums.SerializationEnum;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import factory.singleton.compression.CompressorFactory;
import factory.singleton.serialization.NettySerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import serialization.netty.NettySerializer;

import java.util.Arrays;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        /**
         * lengthFieldOffset: magic code is 4B, and version is 1B, and then full length. so value is 5
         * lengthFieldLength: full length is 4B. so value is 4
         * lengthAdjustment: full length include all data and read 9 bytes before, so the left length is (fullLength-9). so values is -9
         * initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
         */
        super(RpcMessageBodyConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    /**
     * @param maxFrameLength      Maximum frame length. It decide the maximum length of data that can be received.
     *                            If it exceeds, the data will be discarded.
     * @param lengthFieldOffset   Length field offset. The length field is the one that skips the specified length of byte.
     * @param lengthFieldLength   The number of bytes in the length field.
     * @param lengthAdjustment    The compensation value to add to the value of the length field
     * @param initialBytesToStrip Number of bytes skipped.
     *                            If you need to receive all of the header+body data, this value is 0
     *                            if you only want to receive the body data, then you need to skip the number of bytes consumed by the header.
     */
    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        log.info("receive message from " + ctx.channel().remoteAddress());
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcMessageHeaderConstants.HEAD_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("Decode rpc message failed");
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        checkFrame(in);
        // read full length, messageType, serialization type and compress
        int length = in.readInt();
        byte messageType = in.readByte();
        byte serializationCode = in.readByte();
        byte compressionCode = in.readByte();
        RpcMessage rpcMessage = RpcMessage.builder()
                .messageType(messageType)
                .serializationCode(serializationCode)
                .compressionCode(compressionCode).build();

        if (messageType == RpcMessageTypeEnum.HEART_BEAT_REQUEST.getCode()) {
            rpcMessage.setData(RpcMessageBodyConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcMessageTypeEnum.HEART_BEAT_RESPONSE.getCode()) {
            rpcMessage.setData(RpcMessageBodyConstants.PONG);
            return rpcMessage;
        }

        int bodyLength = length - RpcMessageHeaderConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bytes = new byte[bodyLength];
            in.readBytes(bytes);

            // decompress
            CompressionEnum compressionType = CompressionEnum.getType(compressionCode);
            Compressor compressor = CompressorFactory.getInstance(compressionType);
            bytes = compressor.decompress(bytes);

            // deserialize
            SerializationEnum serializationType = SerializationEnum.getType(serializationCode);
            NettySerializer serializer = NettySerializerFactory.getInstance(serializationType);
            Object data = null;
            if (messageType == RpcMessageTypeEnum.REQUEST.getCode()) {
                data = serializer.deserialize(bytes, Request.class);
            } else {
                data = serializer.deserialize(bytes, Response.class);
            }
            rpcMessage.setData(data);
        }
        return rpcMessage;
    }

    private void checkFrame(ByteBuf in) {
        checkMagicNumber(in);
        checkVersion(in);
    }

    private void checkMagicNumber(ByteBuf in) {
        int len = RpcMessageHeaderConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcMessageHeaderConstants.MAGIC_NUMBER[i]) {
                log.error("magic code not match");
                throw new RuntimeException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcMessageHeaderConstants.VERSION) {
            log.error("version not match");
            throw new RuntimeException("wrong version");
        }
    }
}
