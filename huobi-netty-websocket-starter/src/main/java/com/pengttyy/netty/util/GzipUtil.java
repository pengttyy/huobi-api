package com.pengttyy.netty.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
public final class GzipUtil {

    private static final int BUFFER_SIZE = 1024;

    private GzipUtil() {
    }

    /**
     * gzip解压缩
     *
     * @param bytes
     * @return
     * @throws IOException
     */
    public static String decompress(byte[] bytes) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             GZIPInputStream gis = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = gis.read(data, 0, BUFFER_SIZE)) != -1) {
                bos.write(data, 0, count);
            }
            return new String(bos.toByteArray());
        }
    }

    /**
     * 压缩
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public static byte[] compress(String msg)
            throws Exception {
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                ByteArrayInputStream is = new ByteArrayInputStream(msg.getBytes());
        ) {
            int count;
            byte[] data = new byte[BUFFER_SIZE];
            while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
                gos.write(data, 0, count);
            }
            gos.finish();
            return bos.toByteArray();
        }
    }
}
