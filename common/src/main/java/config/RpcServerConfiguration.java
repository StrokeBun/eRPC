package config;

import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import registry.ServiceRegistry;
import registry.ServiceRegistryFactory;
import serialize.serializer.iostream.JdkSerializer;
import serialize.serializer.iostream.IOStreamSerializer;
import serialize.factory.SingletonIOStreamSerializerFactory;
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
    private IOStreamSerializer serializer = DefaultServerConfiguration.DEFAULT_SERIALIZER;
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
         * serializer of RPC
         */
        private static IOStreamSerializer DEFAULT_SERIALIZER;
        /**
         * Default service registry of RPC server
         */
        private static ServiceRegistry DEFAULT_SERVICE_REGISTRY;

        /**
         * keys of configuration file
         */
        private static final String PORT_KEY = "rpc-server-port";
        private static final String SERIALIZE_KEY = "serialize";
        private static final String REGISTRY_SERVER_TYPE_KEY = "registry-server-type";
        private static final String REGISTRY_SERVER_ADDRESS_KEY = "registry-server-address";

        /**
         * init the configuration
         */
        static {
            try {
                Properties properties = PropertiesUtils.loadProperties(SERVER_CONFIGURATION_FILENAME);
                // get the port of RPC server
                RPC_SERVER_PORT = Integer.parseInt(properties.getProperty(PORT_KEY));
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
                DEFAULT_SERVICE_REGISTRY = ServiceRegistryFactory.newInstance(registryServerType, address);
            } catch (NumberFormatException e) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.WRONG_PORT_FORMAT);
            }  catch (IOException e) {
                throw new ConfigurationException(ConfigurationErrorMessageEnum.MISS_SERVER_CONFIGURATION_FILE);
            }
        }
    }
}
