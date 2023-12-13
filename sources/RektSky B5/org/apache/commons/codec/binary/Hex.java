/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.codec.binary;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class Hex
implements BinaryEncoder,
BinaryDecoder {
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final Charset charset;

    public static byte[] decodeHex(String data) throws DecoderException {
        return Hex.decodeHex(data.toCharArray());
    }

    public static byte[] decodeHex(char[] data) throws DecoderException {
        int len = data.length;
        if ((len & 1) != 0) {
            throw new DecoderException("Odd number of characters.");
        }
        byte[] out = new byte[len >> 1];
        int i2 = 0;
        int j2 = 0;
        while (j2 < len) {
            int f2 = Hex.toDigit(data[j2], j2) << 4;
            f2 |= Hex.toDigit(data[++j2], j2);
            ++j2;
            out[i2] = (byte)(f2 & 0xFF);
            ++i2;
        }
        return out;
    }

    public static char[] encodeHex(byte[] data) {
        return Hex.encodeHex(data, true);
    }

    public static char[] encodeHex(ByteBuffer data) {
        return Hex.encodeHex(data, true);
    }

    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return Hex.encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static char[] encodeHex(ByteBuffer data, boolean toLowerCase) {
        return Hex.encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l2 = data.length;
        char[] out = new char[l2 << 1];
        int j2 = 0;
        for (int i2 = 0; i2 < l2; ++i2) {
            out[j2++] = toDigits[(0xF0 & data[i2]) >>> 4];
            out[j2++] = toDigits[0xF & data[i2]];
        }
        return out;
    }

    protected static char[] encodeHex(ByteBuffer data, char[] toDigits) {
        return Hex.encodeHex(data.array(), toDigits);
    }

    public static String encodeHexString(byte[] data) {
        return new String(Hex.encodeHex(data));
    }

    public static String encodeHexString(byte[] data, boolean toLowerCase) {
        return new String(Hex.encodeHex(data, toLowerCase));
    }

    public static String encodeHexString(ByteBuffer data) {
        return new String(Hex.encodeHex(data));
    }

    public static String encodeHexString(ByteBuffer data, boolean toLowerCase) {
        return new String(Hex.encodeHex(data, toLowerCase));
    }

    protected static int toDigit(char ch, int index) throws DecoderException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

    public Hex() {
        this.charset = DEFAULT_CHARSET;
    }

    public Hex(Charset charset) {
        this.charset = charset;
    }

    public Hex(String charsetName) {
        this(Charset.forName(charsetName));
    }

    @Override
    public byte[] decode(byte[] array) throws DecoderException {
        return Hex.decodeHex(new String(array, this.getCharset()).toCharArray());
    }

    public byte[] decode(ByteBuffer buffer) throws DecoderException {
        return Hex.decodeHex(new String(buffer.array(), this.getCharset()).toCharArray());
    }

    @Override
    public Object decode(Object object) throws DecoderException {
        if (object instanceof String) {
            return this.decode(((String)object).toCharArray());
        }
        if (object instanceof byte[]) {
            return this.decode((byte[])object);
        }
        if (object instanceof ByteBuffer) {
            return this.decode((ByteBuffer)object);
        }
        try {
            return Hex.decodeHex((char[])object);
        }
        catch (ClassCastException e2) {
            throw new DecoderException(e2.getMessage(), e2);
        }
    }

    @Override
    public byte[] encode(byte[] array) {
        return Hex.encodeHexString(array).getBytes(this.getCharset());
    }

    public byte[] encode(ByteBuffer array) {
        return Hex.encodeHexString(array).getBytes(this.getCharset());
    }

    @Override
    public Object encode(Object object) throws EncoderException {
        byte[] byteArray;
        if (object instanceof String) {
            byteArray = ((String)object).getBytes(this.getCharset());
        } else if (object instanceof ByteBuffer) {
            byteArray = ((ByteBuffer)object).array();
        } else {
            try {
                byteArray = (byte[])object;
            }
            catch (ClassCastException e2) {
                throw new EncoderException(e2.getMessage(), e2);
            }
        }
        return Hex.encodeHex(byteArray);
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getCharsetName() {
        return this.charset.name();
    }

    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
}

