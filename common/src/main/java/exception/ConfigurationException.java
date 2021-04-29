package exception;

import exception.enums.ConfigurationErrorMessageEnum;

/**
 * @description: Exception thrown by configuration file error.
 * @author: Stroke
 * @date: 2021/04/29
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(ConfigurationErrorMessageEnum messageEnum) {
        super(messageEnum.getMessage());
    }
}
