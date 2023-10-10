/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

public class EncodedText {
    public static final byte TEXT_ENCODING_ISO_8859_1 = 0;
    public static final byte TEXT_ENCODING_UTF_16 = 1;
    public static final byte TEXT_ENCODING_UTF_16BE = 2;
    public static final byte TEXT_ENCODING_UTF_8 = 3;
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    public static final String CHARSET_UTF_16 = "UTF-16LE";
    public static final String CHARSET_UTF_16BE = "UTF-16BE";
    public static final String CHARSET_UTF_8 = "UTF-8";
    private static final String[] characterSets = new String[]{"ISO-8859-1", "UTF-16LE", "UTF-16BE", "UTF-8"};
    private static final byte[] textEncodingFallback = new byte[]{0, 2, 1, 3};
    private static final byte[][] boms = new byte[][]{new byte[0], {-1, -2}, {-2, -1}, new byte[0]};
    private static final byte[][] terminators = new byte[][]{{0}, {0, 0}, {0, 0}, {0}};
    private byte[] value;
    private byte textEncoding;

    public EncodedText(byte by, byte[] byArray) {
        this.textEncoding = by == 1 && EncodedText.textEncodingForBytesFromBOM(byArray) == 2 ? (byte)2 : by;
        this.value = byArray;
        this.stripBomAndTerminator();
    }

    public EncodedText(String string) throws IllegalArgumentException {
        byte[] byArray = textEncodingFallback;
        int n2 = byArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            byte by;
            this.textEncoding = by = byArray[i2];
            this.value = EncodedText.stringToBytes(string, EncodedText.characterSetForTextEncoding(by));
            if (this.value == null || this.toString() == null) continue;
            this.stripBomAndTerminator();
            return;
        }
        throw new IllegalArgumentException("Invalid string, could not find appropriate encoding");
    }

    public EncodedText(String string, byte by) throws IllegalArgumentException, CharacterCodingException {
        this(string);
        this.setTextEncoding(by, true);
    }

    public EncodedText(byte by, String string) {
        this.textEncoding = by;
        this.value = EncodedText.stringToBytes(string, EncodedText.characterSetForTextEncoding(by));
        this.stripBomAndTerminator();
    }

    public EncodedText(byte[] byArray) {
        this(EncodedText.textEncodingForBytesFromBOM(byArray), byArray);
    }

    private static byte textEncodingForBytesFromBOM(byte[] byArray) {
        if (byArray.length >= 2 && byArray[0] == -1 && byArray[1] == -2) {
            return 1;
        }
        if (byArray.length >= 2 && byArray[0] == -2 && byArray[1] == -1) {
            return 2;
        }
        if (byArray.length >= 3 && byArray[0] == -17 && byArray[1] == -69 && byArray[2] == -65) {
            return 3;
        }
        return 0;
    }

    private static String characterSetForTextEncoding(byte by) {
        try {
            return characterSets[by];
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            throw new IllegalArgumentException("Invalid text encoding " + by);
        }
    }

    private void stripBomAndTerminator() {
        int n2;
        int n3 = 0;
        if (this.value.length >= 2 && (this.value[0] == -2 && this.value[1] == -1 || this.value[0] == -1 && this.value[1] == -2)) {
            n3 = 2;
        } else if (this.value.length >= 3 && this.value[0] == -17 && this.value[1] == -69 && this.value[2] == -65) {
            n3 = 3;
        }
        int n4 = 0;
        byte[] byArray = terminators[this.textEncoding];
        if (this.value.length - n3 >= byArray.length) {
            n2 = 1;
            for (int i2 = 0; i2 < byArray.length; ++i2) {
                if (this.value[this.value.length - byArray.length + i2] == byArray[i2]) continue;
                n2 = 0;
                break;
            }
            if (n2 != 0) {
                n4 = byArray.length;
            }
        }
        if (n3 + n4 > 0) {
            n2 = this.value.length - n3 - n4;
            byte[] byArray2 = new byte[n2];
            if (n2 > 0) {
                System.arraycopy(this.value, n3, byArray2, 0, byArray2.length);
            }
            this.value = byArray2;
        }
    }

    public byte getTextEncoding() {
        return this.textEncoding;
    }

    public void setTextEncoding(byte by) throws CharacterCodingException {
        this.setTextEncoding(by, true);
    }

    public void setTextEncoding(byte by, boolean bl) throws CharacterCodingException {
        if (this.textEncoding != by) {
            CharBuffer charBuffer = EncodedText.bytesToCharBuffer(this.value, EncodedText.characterSetForTextEncoding(this.textEncoding));
            byte[] byArray = EncodedText.charBufferToBytes(charBuffer, EncodedText.characterSetForTextEncoding(by));
            this.textEncoding = by;
            this.value = byArray;
        }
    }

    public byte[] getTerminator() {
        return terminators[this.textEncoding];
    }

    public byte[] toBytes() {
        return this.toBytes(false, false);
    }

    public byte[] toBytes(boolean bl) {
        return this.toBytes(bl, false);
    }

    public byte[] toBytes(boolean bl, boolean bl2) {
        byte[] byArray;
        EncodedText.characterSetForTextEncoding(this.textEncoding);
        int n2 = this.value.length + (bl ? boms[this.textEncoding].length : 0) + (bl2 ? this.getTerminator().length : 0);
        if (n2 == this.value.length) {
            return this.value;
        }
        byte[] byArray2 = new byte[n2];
        int n3 = 0;
        if (bl && (byArray = boms[this.textEncoding]).length > 0) {
            System.arraycopy(boms[this.textEncoding], 0, byArray2, n3, boms[this.textEncoding].length);
            n3 += boms[this.textEncoding].length;
        }
        if (this.value.length > 0) {
            System.arraycopy(this.value, 0, byArray2, n3, this.value.length);
            n3 += this.value.length;
        }
        if (bl2 && (byArray = this.getTerminator()).length > 0) {
            System.arraycopy(byArray, 0, byArray2, n3, byArray.length);
        }
        return byArray2;
    }

    public String toString() {
        try {
            return EncodedText.bytesToString(this.value, EncodedText.characterSetForTextEncoding(this.textEncoding));
        }
        catch (CharacterCodingException characterCodingException) {
            return null;
        }
    }

    public String getCharacterSet() {
        return EncodedText.characterSetForTextEncoding(this.textEncoding);
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + this.textEncoding;
        n2 = 31 * n2 + Arrays.hashCode(this.value);
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        EncodedText encodedText = (EncodedText)object;
        if (this.textEncoding != encodedText.textEncoding) {
            return false;
        }
        return Arrays.equals(this.value, encodedText.value);
    }

    private static String bytesToString(byte[] byArray, String string) throws CharacterCodingException {
        CharBuffer charBuffer = EncodedText.bytesToCharBuffer(byArray, string);
        String string2 = charBuffer.toString();
        int n2 = string2.indexOf(0);
        if (n2 == -1) {
            return string2;
        }
        return string2.substring(0, n2);
    }

    protected static CharBuffer bytesToCharBuffer(byte[] byArray, String string) throws CharacterCodingException {
        Charset charset = Charset.forName(string);
        CharsetDecoder charsetDecoder = charset.newDecoder();
        return charsetDecoder.decode(ByteBuffer.wrap(byArray));
    }

    private static byte[] stringToBytes(String string, String string2) {
        try {
            return EncodedText.charBufferToBytes(CharBuffer.wrap(string), string2);
        }
        catch (CharacterCodingException characterCodingException) {
            return null;
        }
    }

    protected static byte[] charBufferToBytes(CharBuffer charBuffer, String string) throws CharacterCodingException {
        Charset charset = Charset.forName(string);
        CharsetEncoder charsetEncoder = charset.newEncoder();
        ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);
        return BufferTools.copyBuffer(byteBuffer.array(), 0, byteBuffer.limit());
    }
}

