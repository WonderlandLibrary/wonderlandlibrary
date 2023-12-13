/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.net.RFC1522Codec;

public class QCodec
extends RFC1522Codec
implements StringEncoder,
StringDecoder {
    private final Charset charset;
    private static final BitSet PRINTABLE_CHARS;
    private static final byte BLANK = 32;
    private static final byte UNDERSCORE = 95;
    private boolean encodeBlanks = false;

    public QCodec() {
        this(Charsets.UTF_8);
    }

    public QCodec(Charset charset) {
        this.charset = charset;
    }

    public QCodec(String charsetName) {
        this(Charset.forName(charsetName));
    }

    @Override
    protected String getEncoding() {
        return "Q";
    }

    @Override
    protected byte[] doEncoding(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
        if (this.encodeBlanks) {
            for (int i2 = 0; i2 < data.length; ++i2) {
                if (data[i2] != 32) continue;
                data[i2] = 95;
            }
        }
        return data;
    }

    @Override
    protected byte[] doDecoding(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        boolean hasUnderscores = false;
        for (byte b2 : bytes) {
            if (b2 != 95) continue;
            hasUnderscores = true;
            break;
        }
        if (hasUnderscores) {
            byte[] tmp = new byte[bytes.length];
            for (int i2 = 0; i2 < bytes.length; ++i2) {
                int b3 = bytes[i2];
                tmp[i2] = b3 != 95 ? b3 : 32;
            }
            return QuotedPrintableCodec.decodeQuotedPrintable(tmp);
        }
        return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
    }

    public String encode(String str, Charset charset) throws EncoderException {
        if (str == null) {
            return null;
        }
        return this.encodeText(str, charset);
    }

    public String encode(String str, String charset) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return this.encodeText(str, charset);
        }
        catch (UnsupportedEncodingException e2) {
            throw new EncoderException(e2.getMessage(), e2);
        }
    }

    @Override
    public String encode(String str) throws EncoderException {
        if (str == null) {
            return null;
        }
        return this.encode(str, this.getCharset());
    }

    @Override
    public String decode(String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return this.decodeText(str);
        }
        catch (UnsupportedEncodingException e2) {
            throw new DecoderException(e2.getMessage(), e2);
        }
    }

    @Override
    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return this.encode((String)obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be encoded using Q codec");
    }

    @Override
    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return this.decode((String)obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be decoded using Q codec");
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public boolean isEncodeBlanks() {
        return this.encodeBlanks;
    }

    public void setEncodeBlanks(boolean b2) {
        this.encodeBlanks = b2;
    }

    static {
        int i2;
        PRINTABLE_CHARS = new BitSet(256);
        PRINTABLE_CHARS.set(32);
        PRINTABLE_CHARS.set(33);
        PRINTABLE_CHARS.set(34);
        PRINTABLE_CHARS.set(35);
        PRINTABLE_CHARS.set(36);
        PRINTABLE_CHARS.set(37);
        PRINTABLE_CHARS.set(38);
        PRINTABLE_CHARS.set(39);
        PRINTABLE_CHARS.set(40);
        PRINTABLE_CHARS.set(41);
        PRINTABLE_CHARS.set(42);
        PRINTABLE_CHARS.set(43);
        PRINTABLE_CHARS.set(44);
        PRINTABLE_CHARS.set(45);
        PRINTABLE_CHARS.set(46);
        PRINTABLE_CHARS.set(47);
        for (i2 = 48; i2 <= 57; ++i2) {
            PRINTABLE_CHARS.set(i2);
        }
        PRINTABLE_CHARS.set(58);
        PRINTABLE_CHARS.set(59);
        PRINTABLE_CHARS.set(60);
        PRINTABLE_CHARS.set(62);
        PRINTABLE_CHARS.set(64);
        for (i2 = 65; i2 <= 90; ++i2) {
            PRINTABLE_CHARS.set(i2);
        }
        PRINTABLE_CHARS.set(91);
        PRINTABLE_CHARS.set(92);
        PRINTABLE_CHARS.set(93);
        PRINTABLE_CHARS.set(94);
        PRINTABLE_CHARS.set(96);
        for (i2 = 97; i2 <= 122; ++i2) {
            PRINTABLE_CHARS.set(i2);
        }
        PRINTABLE_CHARS.set(123);
        PRINTABLE_CHARS.set(124);
        PRINTABLE_CHARS.set(125);
        PRINTABLE_CHARS.set(126);
    }
}

