package compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @description: Compressor based on zip.
 * @author: Stroke
 * @date: 2021/06/01
 */
public class ZipCompressor extends BaseCompressor {

    @Override
    protected byte[] getCompressBytes(byte[] bytes) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(bos)) {
            ZipEntry entry = new ZipEntry("zip");
            entry.setSize(bytes.length);
            zip.putNextEntry(entry);
            zip.write(bytes);
            zip.closeEntry();
            return bos.toByteArray();
        }
    }

    @Override
    protected byte[] getDecompressBytes(byte[] bytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ZipInputStream zip = new ZipInputStream(bis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            while (zip.getNextEntry() != null) {
                byte[] buf = new byte[BUFFER_SIZE];
                int num = -1;
                while ((num = zip.read(buf, 0, buf.length)) != -1) {
                    baos.write(buf, 0, num);
                }
                baos.flush();
            }
            return baos.toByteArray();
        }
    }

    @Override
    protected String getCompressionType() {
        return "zip";
    }

}
