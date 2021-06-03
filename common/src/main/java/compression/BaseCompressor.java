package compression;

import java.io.IOException;

/**
 * @description: A skeletal implementation of the {@link Compressor}
 * @author: Stroke
 * @date: 2021/06/01
 */
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
            throw new RuntimeException(getCompressionType() + " decompress error", e);
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
            throw new RuntimeException(getCompressionType() + " decompress error", e);
        }
    }

    protected abstract byte[] getCompressBytes(byte[] bytes) throws IOException;

    protected abstract byte[] getDecompressBytes(byte[] bytes) throws IOException;

    protected abstract String getCompressionType();
}
