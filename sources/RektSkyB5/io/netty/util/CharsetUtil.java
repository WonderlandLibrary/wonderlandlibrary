/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.internal.InternalThreadLocalMap;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Map;

public final class CharsetUtil {
    public static final Charset UTF_16 = Charset.forName("UTF-16");
    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final Charset US_ASCII = Charset.forName("US-ASCII");

    public static CharsetEncoder getEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        Map<Charset, CharsetEncoder> map = InternalThreadLocalMap.get().charsetEncoderCache();
        CharsetEncoder e2 = map.get(charset);
        if (e2 != null) {
            e2.reset();
            e2.onMalformedInput(CodingErrorAction.REPLACE);
            e2.onUnmappableCharacter(CodingErrorAction.REPLACE);
            return e2;
        }
        e2 = charset.newEncoder();
        e2.onMalformedInput(CodingErrorAction.REPLACE);
        e2.onUnmappableCharacter(CodingErrorAction.REPLACE);
        map.put(charset, e2);
        return e2;
    }

    public static CharsetDecoder getDecoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        Map<Charset, CharsetDecoder> map = InternalThreadLocalMap.get().charsetDecoderCache();
        CharsetDecoder d2 = map.get(charset);
        if (d2 != null) {
            d2.reset();
            d2.onMalformedInput(CodingErrorAction.REPLACE);
            d2.onUnmappableCharacter(CodingErrorAction.REPLACE);
            return d2;
        }
        d2 = charset.newDecoder();
        d2.onMalformedInput(CodingErrorAction.REPLACE);
        d2.onUnmappableCharacter(CodingErrorAction.REPLACE);
        map.put(charset, d2);
        return d2;
    }

    private CharsetUtil() {
    }
}

