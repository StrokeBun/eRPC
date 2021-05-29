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
public final class UnprocessedRequestContext {

    private static final Map<String, CompletableFuture<Response>> UNPROCESSED_REQUEST_FEATURES = new ConcurrentHashMap<>();

    public static void put(String requestId, CompletableFuture<Response> future) {
        UNPROCESSED_REQUEST_FEATURES.put(requestId, future);
    }

    public static void complete(Response response) {
        CompletableFuture<Response> future = UNPROCESSED_REQUEST_FEATURES.remove(response.getResponseId());
        if (future != null) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }
}
