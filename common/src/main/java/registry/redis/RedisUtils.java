package registry.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisSetCommands;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/30
 */
public class RedisUtils {

    private static final Map<String, RedisClient> REDIS_CLIENT_MAP = new ConcurrentHashMap<>();

    public static void add(RedisClient client, String key, String url) {
        try (StatefulRedisConnection connection = client.connect()) {
            RedisSetCommands<String, String> sync = connection.sync();
            sync.sadd(key, url);
        }
    }

    public static Set<String> get(RedisClient client, String key) {
        try (StatefulRedisConnection connection = client.connect()) {
            RedisSetCommands<String, String> sync = connection.sync();
            return sync.smembers(key);
        }
    }

    public static RedisClient getRedisClient(String url) {
        RedisClient client = REDIS_CLIENT_MAP.get(url);
        if (client == null) {
            client = RedisClient.create("redis://" + url);
            REDIS_CLIENT_MAP.put(url, client);
        }
        return client;
    }

    public static void clearClients() {
        for (RedisClient client : REDIS_CLIENT_MAP.values()) {
            client.shutdown();
        }
        REDIS_CLIENT_MAP.clear();
    }

}
