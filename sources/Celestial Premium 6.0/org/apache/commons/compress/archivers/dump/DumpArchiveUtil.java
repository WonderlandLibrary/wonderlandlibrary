/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.dump;

import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;

class DumpArchiveUtil {
    private DumpArchiveUtil() {
    }

    public static int calculateChecksum(byte[] buffer) {
        int calc = 0;
        for (int i = 0; i < 256; ++i) {
            calc += DumpArchiveUtil.convert32(buffer, 4 * i);
        }
        return 84446 - (calc - DumpArchiveUtil.convert32(buffer, 28));
    }

    public static final boolean verify(byte[] buffer) {
        int magic = DumpArchiveUtil.convert32(buffer, 24);
        if (magic != 60012) {
            return false;
        }
        int checksum = DumpArchiveUtil.convert32(buffer, 28);
        return checksum == DumpArchiveUtil.calculateChecksum(buffer);
    }

    public static final int getIno(byte[] buffer) {
        return DumpArchiveUtil.convert32(buffer, 20);
    }

    public static final long convert64(byte[] buffer, int offset) {
        long i = 0L;
        i += (long)buffer[offset + 7] << 56;
        i += (long)buffer[offset + 6] << 48 & 0xFF000000000000L;
        i += (long)buffer[offset + 5] << 40 & 0xFF0000000000L;
        i += (long)buffer[offset + 4] << 32 & 0xFF00000000L;
        i += (long)buffer[offset + 3] << 24 & 0xFF000000L;
        i += (long)buffer[offset + 2] << 16 & 0xFF0000L;
        i += (long)buffer[offset + 1] << 8 & 0xFF00L;
        return i += (long)buffer[offset] & 0xFFL;
    }

    public static final int convert32(byte[] buffer, int offset) {
        int i = 0;
        i = buffer[offset + 3] << 24;
        i += buffer[offset + 2] << 16 & 0xFF0000;
        i += buffer[offset + 1] << 8 & 0xFF00;
        return i += buffer[offset] & 0xFF;
    }

    public static final int convert16(byte[] buffer, int offset) {
        int i = 0;
        i += buffer[offset + 1] << 8 & 0xFF00;
        return i += buffer[offset] & 0xFF;
    }

    static String decode(ZipEncoding encoding, byte[] b, int offset, int len) throws IOException {
        byte[] copy = new byte[len];
        System.arraycopy(b, offset, copy, 0, len);
        return encoding.decode(copy);
    }
}

