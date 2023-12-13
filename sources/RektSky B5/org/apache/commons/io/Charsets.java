/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

public class Charsets {
    @Deprecated
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    @Deprecated
    public static final Charset US_ASCII = StandardCharsets.US_ASCII;
    @Deprecated
    public static final Charset UTF_16 = StandardCharsets.UTF_16;
    @Deprecated
    public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;
    @Deprecated
    public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;
    @Deprecated
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static SortedMap<String, Charset> requiredCharsets() {
        TreeMap<String, Charset> m2 = new TreeMap<String, Charset>(String.CASE_INSENSITIVE_ORDER);
        m2.put(StandardCharsets.ISO_8859_1.name(), StandardCharsets.ISO_8859_1);
        m2.put(StandardCharsets.US_ASCII.name(), StandardCharsets.US_ASCII);
        m2.put(StandardCharsets.UTF_16.name(), StandardCharsets.UTF_16);
        m2.put(StandardCharsets.UTF_16BE.name(), StandardCharsets.UTF_16BE);
        m2.put(StandardCharsets.UTF_16LE.name(), StandardCharsets.UTF_16LE);
        m2.put(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8);
        return Collections.unmodifiableSortedMap(m2);
    }

    public static Charset toCharset(Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }

    public static Charset toCharset(String charset) {
        return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
    }
}

