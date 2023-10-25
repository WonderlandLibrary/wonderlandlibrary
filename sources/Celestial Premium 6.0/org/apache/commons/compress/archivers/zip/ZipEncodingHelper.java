/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.zip;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.FallbackZipEncoding;
import org.apache.commons.compress.archivers.zip.NioZipEncoding;
import org.apache.commons.compress.archivers.zip.Simple8BitZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.utils.Charsets;

public abstract class ZipEncodingHelper {
    private static final Map<String, SimpleEncodingHolder> simpleEncodings;
    private static final byte[] HEX_DIGITS;
    static final String UTF8 = "UTF8";
    static final ZipEncoding UTF8_ZIP_ENCODING;

    static ByteBuffer growBuffer(ByteBuffer b, int newCapacity) {
        b.limit(b.position());
        b.rewind();
        int c2 = b.capacity() * 2;
        ByteBuffer on = ByteBuffer.allocate(c2 < newCapacity ? newCapacity : c2);
        on.put(b);
        return on;
    }

    static void appendSurrogate(ByteBuffer bb, char c) {
        bb.put((byte)37);
        bb.put((byte)85);
        bb.put(HEX_DIGITS[c >> 12 & 0xF]);
        bb.put(HEX_DIGITS[c >> 8 & 0xF]);
        bb.put(HEX_DIGITS[c >> 4 & 0xF]);
        bb.put(HEX_DIGITS[c & 0xF]);
    }

    public static ZipEncoding getZipEncoding(String name) {
        if (ZipEncodingHelper.isUTF8(name)) {
            return UTF8_ZIP_ENCODING;
        }
        if (name == null) {
            return new FallbackZipEncoding();
        }
        SimpleEncodingHolder h = simpleEncodings.get(name);
        if (h != null) {
            return h.getEncoding();
        }
        try {
            Charset cs = Charset.forName(name);
            return new NioZipEncoding(cs);
        }
        catch (UnsupportedCharsetException e) {
            return new FallbackZipEncoding(name);
        }
    }

    static boolean isUTF8(String charsetName) {
        if (charsetName == null) {
            charsetName = System.getProperty("file.encoding");
        }
        if (Charsets.UTF_8.name().equalsIgnoreCase(charsetName)) {
            return true;
        }
        for (String alias : Charsets.UTF_8.aliases()) {
            if (!alias.equalsIgnoreCase(charsetName)) continue;
            return true;
        }
        return false;
    }

    static {
        HashMap<String, SimpleEncodingHolder> se = new HashMap<String, SimpleEncodingHolder>();
        char[] cp437_high_chars = new char[]{'\u00c7', '\u00fc', '\u00e9', '\u00e2', '\u00e4', '\u00e0', '\u00e5', '\u00e7', '\u00ea', '\u00eb', '\u00e8', '\u00ef', '\u00ee', '\u00ec', '\u00c4', '\u00c5', '\u00c9', '\u00e6', '\u00c6', '\u00f4', '\u00f6', '\u00f2', '\u00fb', '\u00f9', '\u00ff', '\u00d6', '\u00dc', '\u00a2', '\u00a3', '\u00a5', '\u20a7', '\u0192', '\u00e1', '\u00ed', '\u00f3', '\u00fa', '\u00f1', '\u00d1', '\u00aa', '\u00ba', '\u00bf', '\u2310', '\u00ac', '\u00bd', '\u00bc', '\u00a1', '\u00ab', '\u00bb', '\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u2561', '\u2562', '\u2556', '\u2555', '\u2563', '\u2551', '\u2557', '\u255d', '\u255c', '\u255b', '\u2510', '\u2514', '\u2534', '\u252c', '\u251c', '\u2500', '\u253c', '\u255e', '\u255f', '\u255a', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256c', '\u2567', '\u2568', '\u2564', '\u2565', '\u2559', '\u2558', '\u2552', '\u2553', '\u256b', '\u256a', '\u2518', '\u250c', '\u2588', '\u2584', '\u258c', '\u2590', '\u2580', '\u03b1', '\u00df', '\u0393', '\u03c0', '\u03a3', '\u03c3', '\u00b5', '\u03c4', '\u03a6', '\u0398', '\u03a9', '\u03b4', '\u221e', '\u03c6', '\u03b5', '\u2229', '\u2261', '\u00b1', '\u2265', '\u2264', '\u2320', '\u2321', '\u00f7', '\u2248', '\u00b0', '\u2219', '\u00b7', '\u221a', '\u207f', '\u00b2', '\u25a0', '\u00a0'};
        SimpleEncodingHolder cp437 = new SimpleEncodingHolder(cp437_high_chars);
        se.put("CP437", cp437);
        se.put("Cp437", cp437);
        se.put("cp437", cp437);
        se.put("IBM437", cp437);
        se.put("ibm437", cp437);
        char[] cp850_high_chars = new char[]{'\u00c7', '\u00fc', '\u00e9', '\u00e2', '\u00e4', '\u00e0', '\u00e5', '\u00e7', '\u00ea', '\u00eb', '\u00e8', '\u00ef', '\u00ee', '\u00ec', '\u00c4', '\u00c5', '\u00c9', '\u00e6', '\u00c6', '\u00f4', '\u00f6', '\u00f2', '\u00fb', '\u00f9', '\u00ff', '\u00d6', '\u00dc', '\u00f8', '\u00a3', '\u00d8', '\u00d7', '\u0192', '\u00e1', '\u00ed', '\u00f3', '\u00fa', '\u00f1', '\u00d1', '\u00aa', '\u00ba', '\u00bf', '\u00ae', '\u00ac', '\u00bd', '\u00bc', '\u00a1', '\u00ab', '\u00bb', '\u2591', '\u2592', '\u2593', '\u2502', '\u2524', '\u00c1', '\u00c2', '\u00c0', '\u00a9', '\u2563', '\u2551', '\u2557', '\u255d', '\u00a2', '\u00a5', '\u2510', '\u2514', '\u2534', '\u252c', '\u251c', '\u2500', '\u253c', '\u00e3', '\u00c3', '\u255a', '\u2554', '\u2569', '\u2566', '\u2560', '\u2550', '\u256c', '\u00a4', '\u00f0', '\u00d0', '\u00ca', '\u00cb', '\u00c8', '\u0131', '\u00cd', '\u00ce', '\u00cf', '\u2518', '\u250c', '\u2588', '\u2584', '\u00a6', '\u00cc', '\u2580', '\u00d3', '\u00df', '\u00d4', '\u00d2', '\u00f5', '\u00d5', '\u00b5', '\u00fe', '\u00de', '\u00da', '\u00db', '\u00d9', '\u00fd', '\u00dd', '\u00af', '\u00b4', '\u00ad', '\u00b1', '\u2017', '\u00be', '\u00b6', '\u00a7', '\u00f7', '\u00b8', '\u00b0', '\u00a8', '\u00b7', '\u00b9', '\u00b3', '\u00b2', '\u25a0', '\u00a0'};
        SimpleEncodingHolder cp850 = new SimpleEncodingHolder(cp850_high_chars);
        se.put("CP850", cp850);
        se.put("Cp850", cp850);
        se.put("cp850", cp850);
        se.put("IBM850", cp850);
        se.put("ibm850", cp850);
        simpleEncodings = Collections.unmodifiableMap(se);
        HEX_DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
        UTF8_ZIP_ENCODING = new FallbackZipEncoding(UTF8);
    }

    private static class SimpleEncodingHolder {
        private final char[] highChars;
        private Simple8BitZipEncoding encoding;

        SimpleEncodingHolder(char[] highChars) {
            this.highChars = highChars;
        }

        public synchronized Simple8BitZipEncoding getEncoding() {
            if (this.encoding == null) {
                this.encoding = new Simple8BitZipEncoding(this.highChars);
            }
            return this.encoding;
        }
    }
}

