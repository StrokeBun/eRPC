package config;

import constants.enums.RegistryEnum;
import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import registry.ServiceRegistry;
import factory.simple.registry.ServiceRegistryFactory;
import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: Configuration of server
 * @author: Stroke
 * @date: 2021/04/21
 */
@Getter
@Setter
@Builder
public class RpcServerConfiguration {

    @Builder.Default
    private int serverPort = DefaultServerConfiguration.RPC_SERVER_PORT;
    @Builder.Default
    private ServiceRegistry serviceRegistry = DefaultServerConfiguration.DEFAULT_SERVICE_REGISTRY;

    private static class DefaultServerConfiguration {
        /**
         * rpc server configuration filename
         */
        private static final String SERVER_CONFIGURATION_FILENAME = "rpc-server-config.properties";

        /**
         * Port of RPC server
         */
        private static int RPC_SERVER_PORT;
        /**
         * Default service registry of RPC server
         */
        private static ServiceRegistry DEFAULT_SERVICE_REGISTRY;

        /**
         * keys of configuration file
         */
        private static final String PORT_KEY = "rpc-server-port";
        private static final String REGISTRY_SERVER_TYPE_KEY = "registry-server-type";
        private static final String REGISTRY_SERVER_ADDRESS_KEY = "registry-server-address";

        /**
         * init the configuration
         */
        static {
            try {
                Properties properties = PropertiesUtils.loadProperties(SERVER_CONFIGURATION_FILENAME);
                parse(properties);
            } catch (IOException e) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_SERVER_CONFIGURATION_FILE);
            }
        }

        private static void parse(Properties properties) {
            parsePort(properties);
            parseRegistry(properties);
        }

        private static void parsePort(Properties properties) {
            try {
                // get the port of RPC server
                RPC_SERVER_PORT = Integer.parseInt(properties.getProperty(PORT_KEY));
            } catch (NumberFormatException e) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.WRONG_PORT_FORMAT);
            }
        }

        private static void parseRegistry(Properties properties) {
            // parse the registry server address
            String address = properties.getProperty(REGISTRY_SERVER_ADDRESS_KEY);
            if (address == null) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_REGISTRY_SERVER_ADDRESS);
            }

            // parse the registry server type
            String registryName = properties.getProperty(REGISTRY_SERVER_TYPE_KEY);
            if (registryName == null) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_REGISTRY_SERVER_TYPE);
            }
            RegistryEnum registryType = RegistryEnum.match(registryName);
            if (registryType == null) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.WRONG_REGISTRY_SERVER_TYPE);
            }

            // init registry registry
            DEFAULT_SERVICE_REGISTRY = ServiceRegistryFactory.newInstance(registryType, address);
        }

    }
}
