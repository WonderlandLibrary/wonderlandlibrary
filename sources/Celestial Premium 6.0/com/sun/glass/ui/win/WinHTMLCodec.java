/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.win.EHTMLReadMode;
import com.sun.glass.ui.win.HTMLCodec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

final class WinHTMLCodec {
    public static final String defaultCharset = "UTF-8";

    WinHTMLCodec() {
    }

    public static byte[] encode(byte[] arrby) {
        return HTMLCodec.convertToHTMLFormat(arrby);
    }

    public static byte[] decode(byte[] arrby) {
        try {
            int n;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrby);
            HTMLCodec hTMLCodec = new HTMLCodec(byteArrayInputStream, EHTMLReadMode.HTML_READ_SELECTION);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(arrby.length);
            while ((n = ((InputStream)hTMLCodec).read()) != -1) {
                byteArrayOutputStream.write(n);
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException iOException) {
            throw new RuntimeException("Unexpected IOException caught", iOException);
        }
    }
}

