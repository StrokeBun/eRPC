package config;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import registry.ServiceDiscovery;
import registry.ServiceDiscoveryFactory;
import serialize.factory.SingletonIOStreamSerializerFactory;
import serialize.serializer.iostream.IOStreamSerializer;
import serialize.serializer.iostream.JdkSerializer;
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
    private IOStreamSerializer serializer = DefaultClientConfiguration.DEFAULT_SERIALIZER;
    @Builder.Default
    private ServiceDiscovery serviceDiscovery = DefaultClientConfiguration.DEFAULT_SERVICE_DISCOVERY;

    private static class DefaultClientConfiguration {
        /**
         * RPC client configuration filename
         */
        private static final String CLIENT_CONFIGURATION_FILENAME = "rpc-client-config.properties";

        /**
         * Default serializer of RPC client
         */
        private static IOStreamSerializer DEFAULT_SERIALIZER;

        /**
         * Default service discovery of RPC client
         */
        private static ServiceDiscovery DEFAULT_SERVICE_DISCOVERY;

        /**
         * Keys of configuration file
         */
        private static final String SERIALIZE_KEY = "serialize";
        private static final String REGISTRY_SERVER_TYPE_KEY = "registry-server-type";
        private static final String REGISTRY_SERVER_ADDRESS_KEY = "registry-server-address";

        /**
         * init the client configuration
         */
        static {
            try {
                Properties properties = PropertiesUtils.loadProperties(CLIENT_CONFIGURATION_FILENAME);
                // get the type of serialize, default jdk serialize
                String serializerType = properties.getProperty(SERIALIZE_KEY);
                DEFAULT_SERIALIZER = serializerType != null ? SingletonIOStreamSerializerFactory.getSerializer(serializerType) : new JdkSerializer();

                String registryServerType = properties.getProperty(REGISTRY_SERVER_TYPE_KEY);
                if (registryServerType == null) {
                    throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_REGISTRY_SERVER_TYPE);
                }
                String address = properties.getProperty(REGISTRY_SERVER_ADDRESS_KEY);
                if (address == null) {
                    throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_REGISTRY_SERVER_ADDRESS);
                }
                DEFAULT_SERVICE_DISCOVERY = ServiceDiscoveryFactory.newInstance(registryServerType, address);
            } catch (IOException e) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_CLIENT_CONFIGURATION_FILE);
            }
        }
    }

}
