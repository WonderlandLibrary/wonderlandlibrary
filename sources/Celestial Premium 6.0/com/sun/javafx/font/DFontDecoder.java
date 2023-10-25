/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.font.FontFileWriter;
import java.io.IOException;
import java.security.AccessController;

class DFontDecoder
extends FontFileWriter {
    private static native long createCTFont(String var0);

    private static native void releaseCTFont(long var0);

    private static native int getCTFontFormat(long var0);

    private static native int[] getCTFontTags(long var0);

    private static native byte[] getCTFontTable(long var0, int var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void decode(String string) throws IOException {
        if (string == null) {
            throw new IOException("Invalid font name");
        }
        long l = 0L;
        try {
            int n;
            int n2;
            int n3;
            l = DFontDecoder.createCTFont(string);
            if (l == 0L) {
                throw new IOException("Failure creating CTFont");
            }
            int n4 = DFontDecoder.getCTFontFormat(l);
            if (n4 != 1953658213 && n4 != 65536 && n4 != 0x4F54544F) {
                throw new IOException("Unsupported Dfont");
            }
            int[] arrn = DFontDecoder.getCTFontTags(l);
            int n5 = arrn.length;
            int n6 = 12 + 16 * n5;
            byte[][] arrarrby = new byte[n5][];
            for (n3 = 0; n3 < arrn.length; ++n3) {
                n2 = arrn[n3];
                arrarrby[n3] = DFontDecoder.getCTFontTable(l, n2);
                n = arrarrby[n3].length;
                n6 += n + 3 & 0xFFFFFFFC;
            }
            DFontDecoder.releaseCTFont(l);
            l = 0L;
            this.setLength(n6);
            this.writeHeader(n4, (short)n5);
            n3 = 12 + 16 * n5;
            for (n2 = 0; n2 < n5; ++n2) {
                n = arrn[n2];
                byte[] arrby = arrarrby[n2];
                this.writeDirectoryEntry(n2, n, 0, n3, arrby.length);
                this.seek(n3);
                this.writeBytes(arrby);
                n3 += arrby.length + 3 & 0xFFFFFFFC;
            }
        }
        finally {
            if (l != 0L) {
                DFontDecoder.releaseCTFont(l);
            }
        }
    }

    static {
        Void void_ = AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("javafx_font");
            return null;
        });
    }
}

