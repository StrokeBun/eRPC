package compression;

/**
 * @description: A compressor used in compressing and decompressing bytes to bytes.
 * @author: Stroke
 * @date: 2021/05/31
 */
public interface Compressor {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
