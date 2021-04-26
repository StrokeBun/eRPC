package config;

import serialize.JdkSerializer;
import serialize.Serializer;
import serialize.SerializerFactory;
import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: Configuration of server
 * @author: Stroke
 * @date: 2021/04/21
 */
public class RpcServerConfiguration {

    private static boolean haveInitialized = false;
    /**
     * Port of RPC server
     */
    private static int RPC_SERVER_PORT;
    /**
     * serializer of RPC
     */
    private static Serializer SERIALIZER;

    /**
     * rpc server configuration filename
     */
    private static final String SERVER_CONFIGURATION_FILENAME = "rpc-server-config.properties";
    /**
     * keys of configuration file
     */
    private static final String PORT_KEY = "rpc-server-port";
    private static final String SERIALIZE_KEY = "serialize";

    /**
     * init the configuration
     */
    private static void init() {
        try {
            Properties properties = PropertiesUtils.loadProperties(SERVER_CONFIGURATION_FILENAME);
            RPC_SERVER_PORT = Integer.parseInt(properties.getProperty(PORT_KEY));
            String serializerType = properties.getProperty(SERIALIZE_KEY);
            SERIALIZER = serializerType != null ? SerializerFactory.getSerializer(serializerType) : new JdkSerializer();
        } catch (NumberFormatException e) {
            System.err.println("wrong port format");
        }  catch (IOException e) {
            System.err.println("miss configuration \"file rpc-server-config.properties\"");
        }
        haveInitialized = true;
    }

    public static int getRpcServerPort() {
        if (!haveInitialized) {
            init();
        }
        return RPC_SERVER_PORT;
    }

    public static Serializer getSerializer() {
        if (!haveInitialized) {
            init();
        }
        return SERIALIZER;
    }
}
