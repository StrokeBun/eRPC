package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class PropertiesUtils {

    public static Properties loadProperties(String filename) throws IOException{
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(filename)) {
            properties.load(inputStream);
        }
        return properties;
    }

}
