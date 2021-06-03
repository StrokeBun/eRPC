package exception;

/**
 * @description: Exception caused by serialization.
 * @author: Stroke
 * @date: 2021/05/09
 */
public class SerializationException extends RuntimeException {

    public SerializationException(String message) {
        super(message);
    }
}
