/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.Utils;

public class QuotedPrintableCodec
implements BinaryEncoder,
BinaryDecoder,
StringEncoder,
StringDecoder {
    private final Charset charset;
    private final boolean strict;
    private static final BitSet PRINTABLE_CHARS;
    private static final byte ESCAPE_CHAR = 61;
    private static final byte TAB = 9;
    private static final byte SPACE = 32;
    private static final byte CR = 13;
    private static final byte LF = 10;
    private static final int SAFE_LENGTH = 73;

    public QuotedPrintableCodec() {
        this(Charsets.UTF_8, false);
    }

    public QuotedPrintableCodec(boolean strict) {
        this(Charsets.UTF_8, strict);
    }

    public QuotedPrintableCodec(Charset charset) {
        this(charset, false);
    }

    public QuotedPrintableCodec(Charset charset, boolean strict) {
        this.charset = charset;
        this.strict = strict;
    }

    public QuotedPrintableCodec(String charsetName) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
        this(Charset.forName(charsetName), false);
    }

    private static final int encodeQuotedPrintable(int b2, ByteArrayOutputStream buffer) {
        buffer.write(61);
        char hex1 = Utils.hexDigit(b2 >> 4);
        char hex2 = Utils.hexDigit(b2);
        buffer.write(hex1);
        buffer.write(hex2);
        return 3;
    }

    private static int getUnsignedOctet(int index, byte[] bytes) {
        int b2 = bytes[index];
        if (b2 < 0) {
            b2 = 256 + b2;
        }
        return b2;
    }

    private static int encodeByte(int b2, boolean encode, ByteArrayOutputStream buffer) {
        if (encode) {
            return QuotedPrintableCodec.encodeQuotedPrintable(b2, buffer);
        }
        buffer.write(b2);
        return 1;
    }

    private static boolean isWhitespace(int b2) {
        return b2 == 32 || b2 == 9;
    }

    public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
        return QuotedPrintableCodec.encodeQuotedPrintable(printable, bytes, false);
    }

    /*
     * WARNING - void declaration
     */
    public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes, boolean strict) {
        if (bytes == null) {
            return null;
        }
        if (printable == null) {
            printable = PRINTABLE_CHARS;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        if (strict) {
            void var7_11;
            boolean encode;
            int pos = 1;
            for (int i2 = 0; i2 < bytes.length - 3; ++i2) {
                int b2 = QuotedPrintableCodec.getUnsignedOctet(i2, bytes);
                if (pos < 73) {
                    pos += QuotedPrintableCodec.encodeByte(b2, !printable.get(b2), buffer);
                    continue;
                }
                QuotedPrintableCodec.encodeByte(b2, !printable.get(b2) || QuotedPrintableCodec.isWhitespace(b2), buffer);
                buffer.write(61);
                buffer.write(13);
                buffer.write(10);
                pos = 1;
            }
            int b3 = QuotedPrintableCodec.getUnsignedOctet(bytes.length - 3, bytes);
            boolean bl = encode = !printable.get(b3) || QuotedPrintableCodec.isWhitespace(b3) && pos > 68;
            if ((pos += QuotedPrintableCodec.encodeByte(b3, encode, buffer)) > 71) {
                buffer.write(61);
                buffer.write(13);
                buffer.write(10);
            }
            int n2 = bytes.length - 2;
            while (var7_11 < bytes.length) {
                b3 = QuotedPrintableCodec.getUnsignedOctet((int)var7_11, bytes);
                encode = !printable.get(b3) || var7_11 > bytes.length - 2 && QuotedPrintableCodec.isWhitespace(b3);
                QuotedPrintableCodec.encodeByte(b3, encode, buffer);
                ++var7_11;
            }
        } else {
            for (int n3 : bytes) {
                int b4 = n3;
                if (b4 < 0) {
                    b4 = 256 + b4;
                }
                if (printable.get(b4)) {
                    buffer.write(b4);
                    continue;
                }
                QuotedPrintableCodec.encodeQuotedPrintable(b4, buffer);
            }
        }
        return buffer.toByteArray();
    }

    public static final byte[] decodeQuotedPrintable(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i2 = 0; i2 < bytes.length; ++i2) {
            byte b2 = bytes[i2];
            if (b2 == 61) {
                try {
                    if (bytes[++i2] == 13) continue;
                    int u2 = Utils.digit16(bytes[i2]);
                    int l2 = Utils.digit16(bytes[++i2]);
                    buffer.write((char)((u2 << 4) + l2));
                    continue;
                }
                catch (ArrayIndexOutOfBoundsException e2) {
                    throw new DecoderException("Invalid quoted-printable encoding", e2);
                }
            }
            if (b2 == 13 || b2 == 10) continue;
            buffer.write(b2);
        }
        return buffer.toByteArray();
    }

    @Override
    public byte[] encode(byte[] bytes) {
        return QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes, this.strict);
    }

    @Override
    public byte[] decode(byte[] bytes) throws DecoderException {
        return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
    }

    @Override
    public String encode(String str) throws EncoderException {
        return this.encode(str, this.getCharset());
    }

    public String decode(String str, Charset charset) throws DecoderException {
        if (str == null) {
            return null;
        }
        return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
    }

    public String decode(String str, String charset) throws DecoderException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
    }

    @Override
    public String decode(String str) throws DecoderException {
        return this.decode(str, this.getCharset());
    }

    @Override
    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return this.encode((byte[])obj);
        }
        if (obj instanceof String) {
            return this.encode((String)obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable encoded");
    }

    @Override
    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return this.decode((byte[])obj);
        }
        if (obj instanceof String) {
            return this.decode((String)obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable decoded");
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public String encode(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
    }

    public String encode(String str, String charset) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
    }

    static {
        int i2;
        PRINTABLE_CHARS = new BitSet(256);
        for (i2 = 33; i2 <= 60; ++i2) {
            PRINTABLE_CHARS.set(i2);
        }
        for (i2 = 62; i2 <= 126; ++i2) {
            PRINTABLE_CHARS.set(i2);
        }
        PRINTABLE_CHARS.set(9);
        PRINTABLE_CHARS.set(32);
    }
}

