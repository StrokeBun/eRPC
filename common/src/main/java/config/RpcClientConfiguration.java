package config;

import serialize.JdkSerializer;
import serialize.Serializer;
import serialize.SerializerFactory;
import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: Configuration of client
 * @author: Stroke
 * @date: 2021/04/21
 */
public class RpcClientConfiguration {

    private static boolean haveInitialized = false;

    /**
     * Serializer of RPC client
     */
    private static Serializer SERIALIZER;

    /**
     * RPC client configuration filename
     */
    private static final String CLIENT_CONFIGURATION_FILENAME = "rpc-client-config.properties";
    /**
     * Keys of configuration file
     */
    private static final String SERIALIZE_KEY = "serialize";

    /**
     * Init the configuration
     */
    private static void init() {
        try {
            Properties properties = PropertiesUtils.loadProperties(CLIENT_CONFIGURATION_FILENAME);
            // get the type of serialize, default jdk serialize
            String serializerType = properties.getProperty(SERIALIZE_KEY);
            SERIALIZER = serializerType != null ? SerializerFactory.getSerializer(serializerType) : new JdkSerializer();

        } catch (IOException e) {
            System.out.println("miss configuration file rpc-client-config.properties");
        } catch (NumberFormatException e) {
            System.out.println("wrong port format");
        }
        haveInitialized = true;
    }


    public static Serializer getSerializer() {
        if (!haveInitialized) {
            init();
        }
        return SERIALIZER;
    }

}
