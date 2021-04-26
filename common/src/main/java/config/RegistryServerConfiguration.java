package config;

import registry.SingletonRegistryFactory;
import serialize.JdkSerializer;
import serialize.SerializerFactory;
import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/26
 */
public class RegistryServerConfiguration {

    private static boolean haveInitialized = false;

    /**
     * Socket address of registry server address
     */
    private static String REGISTRY_ADDRESS;
    private static final String REGISTRY_ADDRESS_KEY = "registry-server-address";
    /**
     * Init the configuration
     */
    private static void init() {
        //Properties properties = PropertiesUtils.loadProperties();
        //REGISTRY_ADDRESS = properties.getProperty(REGISTRY_ADDRESS_KEY);
        //if (REGISTRY_ADDRESS == null || REGISTRY_ADDRESS == null) {
        //    System.out.println("miss registry server information");
        //}
        haveInitialized = true;
    }

    public static String getRegistryAddress() {
        if (!haveInitialized) {
            init();
        }
        return REGISTRY_ADDRESS;
    }
}
