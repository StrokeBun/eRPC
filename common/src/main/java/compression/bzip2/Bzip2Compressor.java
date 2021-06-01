package compression.bzip2;

import compression.BaseCompressor;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @description: compressor based on bzip2.
 * @author: Stroke
 * @date: 2021/06/01
 */
public class Bzip2Compressor extends BaseCompressor {

    @Override
    protected byte[] getCompressBytes(byte[] bytes) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            CBZip2OutputStream bzip2 = new CBZip2OutputStream(bos);
            bzip2.write(bytes);
            bzip2.flush();
            /**
             * CBZip2OutputStream will add bzip2 header when it close, so it is necessary to
             * invoke CBZip2OutputStream.close() before returning compress data.
             */
            bzip2.close();
            return bos.toByteArray();
        }
    }

    @Override
    protected byte[] getDecompressBytes(byte[] bytes) throws IOException {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            CBZip2InputStream bzip2 = new CBZip2InputStream(bis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[BUFFER_SIZE];
            int num = -1;
            while ((num = bzip2.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            baos.flush();
            return baos.toByteArray();
        }
    }

    @Override
    protected String getCompressionType() {
        return "bzip2";
    }
}
