package stub.netty.codec;

import compression.Compressor;
import constants.RpcMessageHeaderConstants;
import constants.enums.CompressionEnum;
import constants.enums.RpcMessageTypeEnum;
import constants.enums.SerializationEnum;
import dto.RpcMessage;
import factory.singleton.compression.CompressorFactory;
import factory.singleton.serialization.NettySerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import serialization.netty.NettySerializer;

/**
 * @description:
 * <p>
 * custom protocol decoder
 * <p>
 * <pre>
 *   0                       4         5                     8             9                    10         11
 *   +-----------------------+---------+---------------------+-------------+--------------------+----------+
 *   |   magic   code        | version |     full length     | messageType | serialization type | compress |
 *   +-----------------------+--------+---------------------+-----------+-----------+----------------------+
 *   |                                                                                                     |
 *   |                                         body                                                        |
 *   |                                                                                                     |
 *   |                                        ... ...                                                      |
 *   +-----------------------------------------------------------------------------------------------------+
 * 4B  magic code   1B version   4B full length    1B messageType   1B codec    1B compress
 * </pre>
 * @author: Stroke
 * @date: 2021/05/23
 */
@Slf4j
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) {
        try {
            writeHeader(out, rpcMessage);
            int fullLength = RpcMessageHeaderConstants.HEAD_LENGTH;
            // if not heart beat message, write the body message
            if (notHeartbeatMessage(rpcMessage)) {
                // write body
                byte[] bodyBytes = getBodyDataBytes(rpcMessage);
                if (bodyBytes != null) {
                    out.writeBytes(bodyBytes);
                }
                fullLength += bodyBytes.length;
            }
            // write full length
            writeLength(out, fullLength);
        } catch (Exception e) {
            log.info("Encode rpc message failed");
        }
    }

    private void writeHeader(ByteBuf out, RpcMessage rpcMessage) {
        out.writeBytes(RpcMessageHeaderConstants.MAGIC_NUMBER);
        out.writeByte(RpcMessageHeaderConstants.VERSION);
        writeInformation(out, rpcMessage);
    }

    private void writeInformation(ByteBuf out, RpcMessage rpcMessage) {
        // leave a place to write the value of full length
        out.writerIndex(out.writerIndex() + 4);

        // write information of message type, serialization and compression
        out.writeByte(rpcMessage.getMessageType());
        out.writeByte(rpcMessage.getSerializationCode());
        out.writeByte(rpcMessage.getCompressionCode());
    }

    private void writeLength(ByteBuf out, int length) {
        int writeIndex = out.writerIndex();
        out.writerIndex(writeIndex - length + RpcMessageHeaderConstants.MAGIC_NUMBER.length + 1);
        out.writeInt(length);
        out.writerIndex(writeIndex);
    }

    private byte[] getBodyDataBytes(RpcMessage rpcMessage) {
        // serialize
        byte serializationCode = rpcMessage.getSerializationCode();
        SerializationEnum serializationType = SerializationEnum.getType(serializationCode);
        NettySerializer serializer = NettySerializerFactory.getInstance(serializationType);
        byte[] bodyBytes = serializer.serialize(rpcMessage.getData());

        // compress
        byte compressionCode = rpcMessage.getCompressionCode();
        CompressionEnum compressionType = CompressionEnum.getType(compressionCode);
        Compressor compressor = CompressorFactory.getInstance(compressionType);
        bodyBytes = compressor.compress(bodyBytes);

        return bodyBytes;
    }

    private boolean notHeartbeatMessage(RpcMessage rpcMessage) {
        byte messageType = rpcMessage.getMessageType();
        return messageType != RpcMessageTypeEnum.HEART_BEAT_REQUEST.getCode() &&
                messageType != RpcMessageTypeEnum.HEART_BEAT_RESPONSE.getCode();
    }
}
