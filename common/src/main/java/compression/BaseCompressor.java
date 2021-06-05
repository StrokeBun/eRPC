package compression;

import exception.CompressionException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @description: A skeletal implementation of the {@link Compressor}
 * @author: Stroke
 * @date: 2021/06/01
 */
@Slf4j
public abstract class BaseCompressor implements Compressor{

    protected static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("the byte array to compress is null");
        }
        try {
            byte[] b = getCompressBytes(bytes);
            return b;
        } catch (IOException e) {
            log.info(getCompressionType() + " compress failed.");
            throw new CompressionException(getCompressionType() + " compress error " + e.getCause());
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("the byte array to decompress is null");
        }
        try {
            byte[] b = getDecompressBytes(bytes);
            return b;
        } catch (IOException e) {
            log.info(getCompressionType() + " decompress failed.");
            throw new CompressionException(getCompressionType() + " decompress error " + e.getCause());
        }
    }

    protected abstract byte[] getCompressBytes(byte[] bytes) throws IOException;

    protected abstract byte[] getDecompressBytes(byte[] bytes) throws IOException;

    protected abstract String getCompressionType();
}
