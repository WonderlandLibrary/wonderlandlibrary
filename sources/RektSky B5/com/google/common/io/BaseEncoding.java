/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckReturnValue
 *  javax.annotation.Nullable
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSink;
import com.google.common.io.CharSource;
import com.google.common.io.GwtWorkarounds;
import com.google.common.math.IntMath;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.RoundingMode;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@Beta
@GwtCompatible(emulated=true)
public abstract class BaseEncoding {
    private static final BaseEncoding BASE64 = new StandardBaseEncoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Character.valueOf('='));
    private static final BaseEncoding BASE64_URL = new StandardBaseEncoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", Character.valueOf('='));
    private static final BaseEncoding BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", Character.valueOf('='));
    private static final BaseEncoding BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", Character.valueOf('='));
    private static final BaseEncoding BASE16 = new StandardBaseEncoding("base16()", "0123456789ABCDEF", null);

    BaseEncoding() {
    }

    public String encode(byte[] bytes) {
        return this.encode(Preconditions.checkNotNull(bytes), 0, bytes.length);
    }

    public final String encode(byte[] bytes, int off, int len) {
        Preconditions.checkNotNull(bytes);
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        GwtWorkarounds.CharOutput result = GwtWorkarounds.stringBuilderOutput(this.maxEncodedSize(len));
        GwtWorkarounds.ByteOutput byteOutput = this.encodingStream(result);
        try {
            for (int i2 = 0; i2 < len; ++i2) {
                byteOutput.write(bytes[off + i2]);
            }
            byteOutput.close();
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)"impossible");
        }
        return result.toString();
    }

    @GwtIncompatible(value="Writer,OutputStream")
    public final OutputStream encodingStream(Writer writer) {
        return GwtWorkarounds.asOutputStream(this.encodingStream(GwtWorkarounds.asCharOutput(writer)));
    }

    @GwtIncompatible(value="ByteSink,CharSink")
    public final ByteSink encodingSink(final CharSink encodedSink) {
        Preconditions.checkNotNull(encodedSink);
        return new ByteSink(){

            @Override
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(encodedSink.openStream());
            }
        };
    }

    private static byte[] extract(byte[] result, int length) {
        if (length == result.length) {
            return result;
        }
        byte[] trunc = new byte[length];
        System.arraycopy(result, 0, trunc, 0, length);
        return trunc;
    }

    public final byte[] decode(CharSequence chars) {
        try {
            return this.decodeChecked(chars);
        }
        catch (DecodingException badInput) {
            throw new IllegalArgumentException(badInput);
        }
    }

    final byte[] decodeChecked(CharSequence chars) throws DecodingException {
        chars = this.padding().trimTrailingFrom(chars);
        GwtWorkarounds.ByteInput decodedInput = this.decodingStream(GwtWorkarounds.asCharInput(chars));
        byte[] tmp = new byte[this.maxDecodedSize(chars.length())];
        int index = 0;
        try {
            int i2 = decodedInput.read();
            while (i2 != -1) {
                tmp[index++] = (byte)i2;
                i2 = decodedInput.read();
            }
        }
        catch (DecodingException badInput) {
            throw badInput;
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
        return BaseEncoding.extract(tmp, index);
    }

    @GwtIncompatible(value="Reader,InputStream")
    public final InputStream decodingStream(Reader reader) {
        return GwtWorkarounds.asInputStream(this.decodingStream(GwtWorkarounds.asCharInput(reader)));
    }

    @GwtIncompatible(value="ByteSource,CharSource")
    public final ByteSource decodingSource(final CharSource encodedSource) {
        Preconditions.checkNotNull(encodedSource);
        return new ByteSource(){

            @Override
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(encodedSource.openStream());
            }
        };
    }

    abstract int maxEncodedSize(int var1);

    abstract GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput var1);

    abstract int maxDecodedSize(int var1);

    abstract GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput var1);

    abstract CharMatcher padding();

    @CheckReturnValue
    public abstract BaseEncoding omitPadding();

    @CheckReturnValue
    public abstract BaseEncoding withPadChar(char var1);

    @CheckReturnValue
    public abstract BaseEncoding withSeparator(String var1, int var2);

    @CheckReturnValue
    public abstract BaseEncoding upperCase();

    @CheckReturnValue
    public abstract BaseEncoding lowerCase();

    public static BaseEncoding base64() {
        return BASE64;
    }

    public static BaseEncoding base64Url() {
        return BASE64_URL;
    }

    public static BaseEncoding base32() {
        return BASE32;
    }

    public static BaseEncoding base32Hex() {
        return BASE32_HEX;
    }

    public static BaseEncoding base16() {
        return BASE16;
    }

    static GwtWorkarounds.CharInput ignoringInput(final GwtWorkarounds.CharInput delegate, final CharMatcher toIgnore) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(toIgnore);
        return new GwtWorkarounds.CharInput(){

            @Override
            public int read() throws IOException {
                int readChar;
                while ((readChar = delegate.read()) != -1 && toIgnore.matches((char)readChar)) {
                }
                return readChar;
            }

            @Override
            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    static GwtWorkarounds.CharOutput separatingOutput(final GwtWorkarounds.CharOutput delegate, final String separator, final int afterEveryChars) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(afterEveryChars > 0);
        return new GwtWorkarounds.CharOutput(){
            int charsUntilSeparator;
            {
                this.charsUntilSeparator = afterEveryChars;
            }

            @Override
            public void write(char c2) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    for (int i2 = 0; i2 < separator.length(); ++i2) {
                        delegate.write(separator.charAt(i2));
                    }
                    this.charsUntilSeparator = afterEveryChars;
                }
                delegate.write(c2);
                --this.charsUntilSeparator;
            }

            @Override
            public void flush() throws IOException {
                delegate.flush();
            }

            @Override
            public void close() throws IOException {
                delegate.close();
            }
        };
    }

    static final class SeparatedBaseEncoding
    extends BaseEncoding {
        private final BaseEncoding delegate;
        private final String separator;
        private final int afterEveryChars;
        private final CharMatcher separatorChars;

        SeparatedBaseEncoding(BaseEncoding delegate, String separator, int afterEveryChars) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.separator = Preconditions.checkNotNull(separator);
            this.afterEveryChars = afterEveryChars;
            Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
            this.separatorChars = CharMatcher.anyOf(separator).precomputed();
        }

        @Override
        CharMatcher padding() {
            return this.delegate.padding();
        }

        @Override
        int maxEncodedSize(int bytes) {
            int unseparatedSize = this.delegate.maxEncodedSize(bytes);
            return unseparatedSize + this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
        }

        @Override
        GwtWorkarounds.ByteOutput encodingStream(GwtWorkarounds.CharOutput output) {
            return this.delegate.encodingStream(SeparatedBaseEncoding.separatingOutput(output, this.separator, this.afterEveryChars));
        }

        @Override
        int maxDecodedSize(int chars) {
            return this.delegate.maxDecodedSize(chars);
        }

        @Override
        GwtWorkarounds.ByteInput decodingStream(GwtWorkarounds.CharInput input) {
            return this.delegate.decodingStream(SeparatedBaseEncoding.ignoringInput(input, this.separatorChars));
        }

        @Override
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override
        public BaseEncoding withPadChar(char padChar) {
            return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
        }

        @Override
        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            throw new UnsupportedOperationException("Already have a separator");
        }

        @Override
        public BaseEncoding upperCase() {
            return this.delegate.upperCase().withSeparator(this.separator, this.afterEveryChars);
        }

        @Override
        public BaseEncoding lowerCase() {
            return this.delegate.lowerCase().withSeparator(this.separator, this.afterEveryChars);
        }

        public String toString() {
            return this.delegate.toString() + ".withSeparator(\"" + this.separator + "\", " + this.afterEveryChars + ")";
        }
    }

    static final class StandardBaseEncoding
    extends BaseEncoding {
        private final Alphabet alphabet;
        @Nullable
        private final Character paddingChar;
        private transient BaseEncoding upperCase;
        private transient BaseEncoding lowerCase;

        StandardBaseEncoding(String name, String alphabetChars, @Nullable Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }

        StandardBaseEncoding(Alphabet alphabet, @Nullable Character paddingChar) {
            this.alphabet = Preconditions.checkNotNull(alphabet);
            Preconditions.checkArgument(paddingChar == null || !alphabet.matches(paddingChar.charValue()), "Padding character %s was already in alphabet", paddingChar);
            this.paddingChar = paddingChar;
        }

        @Override
        CharMatcher padding() {
            return this.paddingChar == null ? CharMatcher.NONE : CharMatcher.is(this.paddingChar.charValue());
        }

        @Override
        int maxEncodedSize(int bytes) {
            return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }

        @Override
        GwtWorkarounds.ByteOutput encodingStream(final GwtWorkarounds.CharOutput out) {
            Preconditions.checkNotNull(out);
            return new GwtWorkarounds.ByteOutput(){
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;

                @Override
                public void write(byte b2) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer |= b2 & 0xFF;
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.bitsPerChar) {
                        int charIndex = this.bitBuffer >> this.bitBufferLength - ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.bitsPerChar & ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        ++this.writtenChars;
                        this.bitBufferLength -= ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.bitsPerChar;
                    }
                }

                @Override
                public void flush() throws IOException {
                    out.flush();
                }

                @Override
                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        int charIndex = this.bitBuffer << ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.bitsPerChar - this.bitBufferLength & ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        ++this.writtenChars;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.charsPerChunk != 0) {
                                out.write(StandardBaseEncoding.this.paddingChar.charValue());
                                ++this.writtenChars;
                            }
                        }
                    }
                    out.close();
                }
            };
        }

        @Override
        int maxDecodedSize(int chars) {
            return (int)(((long)this.alphabet.bitsPerChar * (long)chars + 7L) / 8L);
        }

        @Override
        GwtWorkarounds.ByteInput decodingStream(final GwtWorkarounds.CharInput reader) {
            Preconditions.checkNotNull(reader);
            return new GwtWorkarounds.ByteInput(){
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int readChars = 0;
                boolean hitPadding = false;
                final CharMatcher paddingMatcher = StandardBaseEncoding.this.padding();

                @Override
                public int read() throws IOException {
                    while (true) {
                        int readChar;
                        if ((readChar = reader.read()) == -1) {
                            if (!this.hitPadding && !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                                throw new DecodingException("Invalid input length " + this.readChars);
                            }
                            return -1;
                        }
                        ++this.readChars;
                        char ch = (char)readChar;
                        if (this.paddingMatcher.matches(ch)) {
                            if (!(this.hitPadding || this.readChars != 1 && StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                                throw new DecodingException("Padding cannot start at index " + this.readChars);
                            }
                            this.hitPadding = true;
                            continue;
                        }
                        if (this.hitPadding) {
                            throw new DecodingException("Expected padding character but found '" + ch + "' at index " + this.readChars);
                        }
                        this.bitBuffer <<= ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.bitsPerChar;
                        this.bitBuffer |= StandardBaseEncoding.this.alphabet.decode(ch);
                        this.bitBufferLength += ((StandardBaseEncoding)StandardBaseEncoding.this).alphabet.bitsPerChar;
                        if (this.bitBufferLength >= 8) break;
                    }
                    this.bitBufferLength -= 8;
                    return this.bitBuffer >> this.bitBufferLength & 0xFF;
                }

                @Override
                public void close() throws IOException {
                    reader.close();
                }
            };
        }

        @Override
        public BaseEncoding omitPadding() {
            return this.paddingChar == null ? this : new StandardBaseEncoding(this.alphabet, null);
        }

        @Override
        public BaseEncoding withPadChar(char padChar) {
            if (8 % this.alphabet.bitsPerChar == 0 || this.paddingChar != null && this.paddingChar.charValue() == padChar) {
                return this;
            }
            return new StandardBaseEncoding(this.alphabet, Character.valueOf(padChar));
        }

        @Override
        public BaseEncoding withSeparator(String separator, int afterEveryChars) {
            Preconditions.checkNotNull(separator);
            Preconditions.checkArgument(this.padding().or(this.alphabet).matchesNoneOf(separator), "Separator cannot contain alphabet or padding characters");
            return new SeparatedBaseEncoding(this, separator, afterEveryChars);
        }

        @Override
        public BaseEncoding upperCase() {
            BaseEncoding result = this.upperCase;
            if (result == null) {
                Alphabet upper = this.alphabet.upperCase();
                this.upperCase = upper == this.alphabet ? this : new StandardBaseEncoding(upper, this.paddingChar);
                result = this.upperCase;
            }
            return result;
        }

        @Override
        public BaseEncoding lowerCase() {
            BaseEncoding result = this.lowerCase;
            if (result == null) {
                Alphabet lower = this.alphabet.lowerCase();
                this.lowerCase = lower == this.alphabet ? this : new StandardBaseEncoding(lower, this.paddingChar);
                result = this.lowerCase;
            }
            return result;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("BaseEncoding.");
            builder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    builder.append(".omitPadding()");
                } else {
                    builder.append(".withPadChar(").append(this.paddingChar).append(')');
                }
            }
            return builder.toString();
        }
    }

    private static final class Alphabet
    extends CharMatcher {
        private final String name;
        private final char[] chars;
        final int mask;
        final int bitsPerChar;
        final int charsPerChunk;
        final int bytesPerChunk;
        private final byte[] decodabet;
        private final boolean[] validPadding;

        Alphabet(String name, char[] chars) {
            this.name = Preconditions.checkNotNull(name);
            this.chars = Preconditions.checkNotNull(chars);
            try {
                this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
            }
            catch (ArithmeticException e2) {
                throw new IllegalArgumentException("Illegal alphabet length " + chars.length, e2);
            }
            int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
            this.charsPerChunk = 8 / gcd;
            this.bytesPerChunk = this.bitsPerChar / gcd;
            this.mask = chars.length - 1;
            byte[] decodabet = new byte[128];
            Arrays.fill(decodabet, (byte)-1);
            for (int i2 = 0; i2 < chars.length; ++i2) {
                char c2 = chars[i2];
                Preconditions.checkArgument(CharMatcher.ASCII.matches(c2), "Non-ASCII character: %s", Character.valueOf(c2));
                Preconditions.checkArgument(decodabet[c2] == -1, "Duplicate character: %s", Character.valueOf(c2));
                decodabet[c2] = (byte)i2;
            }
            this.decodabet = decodabet;
            boolean[] validPadding = new boolean[this.charsPerChunk];
            for (int i3 = 0; i3 < this.bytesPerChunk; ++i3) {
                validPadding[IntMath.divide((int)(i3 * 8), (int)this.bitsPerChar, (RoundingMode)RoundingMode.CEILING)] = true;
            }
            this.validPadding = validPadding;
        }

        char encode(int bits) {
            return this.chars[bits];
        }

        boolean isValidPaddingStartPosition(int index) {
            return this.validPadding[index % this.charsPerChunk];
        }

        int decode(char ch) throws IOException {
            if (ch > '\u007f' || this.decodabet[ch] == -1) {
                throw new DecodingException("Unrecognized character: " + ch);
            }
            return this.decodabet[ch];
        }

        private boolean hasLowerCase() {
            for (char c2 : this.chars) {
                if (!Ascii.isLowerCase(c2)) continue;
                return true;
            }
            return false;
        }

        private boolean hasUpperCase() {
            for (char c2 : this.chars) {
                if (!Ascii.isUpperCase(c2)) continue;
                return true;
            }
            return false;
        }

        Alphabet upperCase() {
            if (!this.hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!this.hasUpperCase(), "Cannot call upperCase() on a mixed-case alphabet");
            char[] upperCased = new char[this.chars.length];
            for (int i2 = 0; i2 < this.chars.length; ++i2) {
                upperCased[i2] = Ascii.toUpperCase(this.chars[i2]);
            }
            return new Alphabet(this.name + ".upperCase()", upperCased);
        }

        Alphabet lowerCase() {
            if (!this.hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!this.hasLowerCase(), "Cannot call lowerCase() on a mixed-case alphabet");
            char[] lowerCased = new char[this.chars.length];
            for (int i2 = 0; i2 < this.chars.length; ++i2) {
                lowerCased[i2] = Ascii.toLowerCase(this.chars[i2]);
            }
            return new Alphabet(this.name + ".lowerCase()", lowerCased);
        }

        @Override
        public boolean matches(char c2) {
            return CharMatcher.ASCII.matches(c2) && this.decodabet[c2] != -1;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static final class DecodingException
    extends IOException {
        DecodingException(String message) {
            super(message);
        }

        DecodingException(Throwable cause) {
            super(cause);
        }
    }
}

