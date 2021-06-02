package stub.netty.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/06/02
 */
public class ChannelProvider {

    private Map<InetSocketAddress, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress address) {
        Channel channel = channelMap.get(address);
        // if the connection is not available, remove it
        if (channel != null && !channel.isActive()) {
            channelMap.remove(address);
            channel = null;
        }
        return channel;
    }

    public void set(InetSocketAddress address, Channel channel) {
        channelMap.put(address, channel);
    }
}
