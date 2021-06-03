package factory.singleton.compression;

import compression.Compressor;
import compression.Bzip2Compressor;
import compression.GzipCompressor;
import compression.ZipCompressor;
import constants.enums.CompressionEnum;
import exception.ConfigurationException;
import exception.enums.ConfigurationErrorMessageEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: A singleton factory to create compressor.
 * @author: Stroke
 * @date: 2021/06/01
 */
public final class CompressorFactory {

    private static Map<CompressionEnum, Compressor> COMPRESSOR_MAP = new ConcurrentHashMap<CompressionEnum, Compressor>() {
        {
            put(CompressionEnum.ZIP, new ZipCompressor());
            put(CompressionEnum.GZIP, new GzipCompressor());
            put(CompressionEnum.BZIP2, new Bzip2Compressor());
        }
    };

    public static Compressor getInstance(CompressionEnum type) {
        final Compressor compressor = COMPRESSOR_MAP.get(type);
        if (compressor == null) {
            throw new ConfigurationException(ConfigurationErrorMessageEnum.UNSUPPORTED_COMPRESSION_TYPE);
        }
        return compressor;
    }

    private CompressorFactory() {

    }
}
