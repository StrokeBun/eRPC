package stub;

import dto.Request;
import dto.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/23
 */
public final class ServerStubUtils {

    /**
     * Generate the RPC response.
     * @param request RPC request from client
     * @return RPC response
     */
    public static Response getResponse(Request request) {
        Object result = null;
        Response response = new Response();
        response.setResponseId(request.getRequestId());
        try {
            result = invoke(request);
        } catch (ClassNotFoundException e) {
            response.setError("class not found");
        } catch (NoSuchMethodException e) {
            response.setError("method not found");
        } catch (Exception e){
            response.setError("function inner error");
        } finally {
            response.setResult(result);
        }
        return response;
    }

    /**
     * Use reflection to invoke the method.
     * @param request RPC request from client
     * @return result object
     */
    private static Object invoke(Request request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class clazz = Class.forName(className);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod(methodName, request.getParametersType());
        return method.invoke(object, request.getParametersValue());
    }
}
