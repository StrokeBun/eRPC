package config;

import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: Configuration of client
 * @author: Stroke
 * @date: 2021/04/21
 */
public class RpcClientConfig {
    private static boolean haveInitialized = false;

    /**
     * IP of RPC Server
     */
    private static String RPC_SERVER_IP;
    /**
     * port of RPC Server
     */
    private static int RPC_SERVER_PORT;

    /**
     * rpc client configuration filename
     */
    private static final String CLIENT_CONFIGURATION_FILENAME = "rpc-client-config.properties";
    /**
     * keys of configuration file
     */
    private static final String IP_KEY = "rpc-server-ip";
    private static final String PORT_KEY = "rpc-server-port";

    /**
     * init the configuration
     */
    private static void init() {
        try {
            Properties properties = PropertiesUtils.loadProperties(CLIENT_CONFIGURATION_FILENAME);
            RPC_SERVER_IP = properties.getProperty(IP_KEY);
            if (RPC_SERVER_IP == null) {
                System.out.println("rpc-client-config.properties miss ip");
            }
            RPC_SERVER_PORT = Integer.parseInt(properties.getProperty(PORT_KEY));

        } catch (IOException e) {
            System.out.println("miss configuration file rpc-client-config.properties");
        } catch (NumberFormatException e) {
            System.out.println("wrong port format");
        }
        haveInitialized = true;
    }

    public static int getRpcServerPort() {
        if (!haveInitialized) {
            init();
        }
        return RPC_SERVER_PORT;
    }

    public static String getRpcServerIp() {
        if (!haveInitialized) {
            init();
        }
        return RPC_SERVER_IP;
    }
}
