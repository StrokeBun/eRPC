package config;

import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: Configuration of server
 * @author: Stroke
 * @date: 2021/04/21
 */
public class RpcServerConfig {
    private static boolean haveInitialized = false;
    private static int RPC_SERVER_PORT;

    /**
     * rpc server configuration filename
     */
    private static final String SERVER_CONFIGURATION_FILENAME = "rpc-server-config.properties";
    /**
     * keys of configuration file
     */
    private static final String PORT_KEY = "rpc-server-port";

    /**
     * init the configuration
     */
    private static void init() {
        try {
            Properties properties = PropertiesUtils.loadProperties(SERVER_CONFIGURATION_FILENAME);
            RPC_SERVER_PORT = Integer.parseInt(properties.getProperty(PORT_KEY));
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
}
