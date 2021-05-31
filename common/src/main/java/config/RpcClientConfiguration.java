package config;

import constants.enums.RegistryEnum;
import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import registry.ServiceDiscovery;
import factory.simple.registry.ServiceDiscoveryFactory;
import util.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: Configuration of client.
 * @author: Stroke
 * @date: 2021/04/21
 */
@Getter
@Setter
@Builder
public class RpcClientConfiguration {

    @Builder.Default
    private ServiceDiscovery serviceDiscovery = DefaultClientConfiguration.DEFAULT_SERVICE_DISCOVERY;

    private static class DefaultClientConfiguration {
        /**
         * RPC client configuration filename
         */
        private static final String CLIENT_CONFIGURATION_FILENAME = "rpc-client-config.properties";

        /**
         * Default service discovery of RPC client
         */
        private static ServiceDiscovery DEFAULT_SERVICE_DISCOVERY;

        /**
         * Keys of configuration file
         */
        private static final String REGISTRY_SERVER_TYPE_KEY = "registry-server-type";
        private static final String REGISTRY_SERVER_ADDRESS_KEY = "registry-server-address";

        /**
         * init the client configuration
         */
        static {
            try {
                Properties properties = PropertiesUtils.loadProperties(CLIENT_CONFIGURATION_FILENAME);
                parse(properties);
            } catch (IOException e) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_CLIENT_CONFIGURATION_FILE);
            }
        }

        private static void parse(Properties properties) {
            parseRegistry(properties);
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

            // init registry discovery
            DEFAULT_SERVICE_DISCOVERY = ServiceDiscoveryFactory.newInstance(registryType, address);
        }
    }

}
