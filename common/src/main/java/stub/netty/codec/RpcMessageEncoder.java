package stub.netty.codec;

import dto.RpcMessage;
import constants.RpcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serialize.factory.NettySerializerFactory;
import serialize.factory.SerializationTypeEnum;
import serialize.serializer.netty.NettySerializer;

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
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf out) {
        try {
            out.writeBytes(RpcConstants.MAGIC_NUMBER);
            out.writeByte(RpcConstants.VERSION);
            // leave a place to write the value of full length
            out.writerIndex(out.writerIndex() + 4);
            out.writeByte(rpcMessage.getMessageType());
            byte serializationType = rpcMessage.getSerializationType();
            out.writeByte(serializationType);
            // TODO: change compress type
            int compress = 1;
            out.writeByte(compress);

            // serialize
            SerializationTypeEnum type = SerializationTypeEnum.getType(serializationType);
            NettySerializer serializer = NettySerializerFactory.getInstance(type);
            byte[] bodyBytes = serializer.serialize(rpcMessage.getData());

            // write data and full length
            int fullLength = RpcConstants.HEAD_LENGTH + bodyBytes.length;
            if (bodyBytes != null) {
                out.writeBytes(bodyBytes);
            }
            int writeIndex = out.writerIndex();
            out.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            out.writeInt(fullLength);
            out.writerIndex(writeIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
