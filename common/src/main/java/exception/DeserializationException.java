package exception;

/**
 * @description: Exception caused by Deserialization.
 * @author: Stroke
 * @date: 2021/06/04
 */
public class DeserializationException extends RuntimeException {

    public DeserializationException(String message) {
        super("Deserialization failed: " + message);
    }

}
