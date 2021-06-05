package exception;

/**
 * @description: Exception caused by compression and decompression.
 * @author: Stroke
 * @date: 2021/06/05
 */
public class CompressionException extends RuntimeException {

    public CompressionException(String message) {
        super(message);
    }

}
