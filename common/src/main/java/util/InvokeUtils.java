package util;

import dto.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/05/29
 */
public class InvokeUtils {

    /**
     * Use reflection to invoke the method.
     * @param request RPC request from client
     * @return result object
     */
    public static Object invoke(Request request) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class clazz = Class.forName(className);
        Object object = clazz.newInstance();
        Method method = clazz.getMethod(methodName, request.getParametersType());
        return method.invoke(object, request.getParametersValue());
    }

}
