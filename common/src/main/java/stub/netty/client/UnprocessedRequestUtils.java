package stub.netty.client;

import dto.Request;
import dto.Response;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/27
 */
public final class UnprocessedRequestUtils {

    private static final Map<String, CompletableFuture<Response>> UNPROCESSED_REQUEST_FEATURE = new ConcurrentHashMap<>();

    public static void put(String requestId, CompletableFuture<Response> future) {
        UNPROCESSED_REQUEST_FEATURE.put(requestId, future);
    }

    public static void complete(Response response) {
        CompletableFuture<Response> future = UNPROCESSED_REQUEST_FEATURE.remove(response.getResponseId());
        if (future != null) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }
}
