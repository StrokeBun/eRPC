package factory.singleton;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: factory of singleton object
 * @author: Stroke
 * @date: 2021/05/31
 */
public class SingletonFactory {

    private static final Map<Class, Object> SINGLETON_OBJECTS = new ConcurrentHashMap<>();

    public static final <T> T getInstance(Class<T> clazz) {
       if (clazz == null) {
           throw new IllegalArgumentException();
       }
       try {
           Object instance = SINGLETON_OBJECTS.get(clazz);
           if (instance == null) {
               instance = clazz.getDeclaredConstructor().newInstance();
               SINGLETON_OBJECTS.put(clazz, instance);
           }
           return (T)instance;
       } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
           throw new RuntimeException(e.getMessage(), e);
       }
    }
}
