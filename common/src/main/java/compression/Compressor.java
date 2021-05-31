package compression;

/**
 * @description: interface of compressor
 * @author: Stroke
 * @date: 2021/05/31
 */
public interface Compressor {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);
}
