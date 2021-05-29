package stub.netty.codec;

import constants.RpcConstants;
import dto.Request;
import dto.Response;
import dto.RpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import serialize.factory.NettySerializerFactory;
import serialize.factory.SerializationTypeEnum;
import serialize.serializer.netty.NettySerializer;

import java.util.Arrays;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/25
 */
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        /**
         * lengthFieldOffset: magic code is 4B, and version is 1B, and then full length. so value is 5
         * lengthFieldLength: full length is 4B. so value is 4
         * lengthAdjustment: full length include all data and read 9 bytes before, so the left length is (fullLength-9). so values is -9
         * initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
         */
        super(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
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
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.HEAD_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    e.printStackTrace();
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
        byte serializationType = in.readByte();
        byte compress = in.readByte();
        RpcMessage message = RpcMessage.builder()
                .messageType(messageType)
                .serializationType(serializationType)
                .compress(compress).build();

        int bodyLength = length - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bytes = new byte[bodyLength];
            in.readBytes(bytes);

            // deserialize the object
            SerializationTypeEnum type = SerializationTypeEnum.getType(serializationType);
            NettySerializer serializer = NettySerializerFactory.getInstance(type);
            Object data = null;
            if (messageType == RpcConstants.REQUEST_TYPE) {
                data = serializer.deserialize(bytes, Request.class);
            } else {
                data = serializer.deserialize(bytes, Response.class);
            }
            message.setData(data);
        }
        return message;
    }

    private void checkFrame(ByteBuf in) {
        checkMagicNumber(in);
        checkVersion(in);
    }

    private void checkMagicNumber(ByteBuf in) {
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new RuntimeException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("wrong version");
        }
    }
}
