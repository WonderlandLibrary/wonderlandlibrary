// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.common.base.Objects;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.base.Ascii;
import java.util.Arrays;
import com.google.common.math.IntMath;
import java.math.RoundingMode;
import javax.annotation.CheckForNull;
import java.io.InputStream;
import java.io.Reader;
import com.google.common.annotations.GwtIncompatible;
import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public abstract class BaseEncoding
{
    private static final BaseEncoding BASE64;
    private static final BaseEncoding BASE64_URL;
    private static final BaseEncoding BASE32;
    private static final BaseEncoding BASE32_HEX;
    private static final BaseEncoding BASE16;
    
    BaseEncoding() {
    }
    
    public String encode(final byte[] bytes) {
        return this.encode(bytes, 0, bytes.length);
    }
    
    public final String encode(final byte[] bytes, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, bytes.length);
        final StringBuilder result = new StringBuilder(this.maxEncodedSize(len));
        try {
            this.encodeTo(result, bytes, off, len);
        }
        catch (IOException impossible) {
            throw new AssertionError((Object)impossible);
        }
        return result.toString();
    }
    
    @GwtIncompatible
    public abstract OutputStream encodingStream(final Writer p0);
    
    @GwtIncompatible
    public final ByteSink encodingSink(final CharSink encodedSink) {
        Preconditions.checkNotNull(encodedSink);
        return new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                return BaseEncoding.this.encodingStream(encodedSink.openStream());
            }
        };
    }
    
    private static byte[] extract(final byte[] result, final int length) {
        if (length == result.length) {
            return result;
        }
        final byte[] trunc = new byte[length];
        System.arraycopy(result, 0, trunc, 0, length);
        return trunc;
    }
    
    public abstract boolean canDecode(final CharSequence p0);
    
    public final byte[] decode(final CharSequence chars) {
        try {
            return this.decodeChecked(chars);
        }
        catch (DecodingException badInput) {
            throw new IllegalArgumentException(badInput);
        }
    }
    
    final byte[] decodeChecked(CharSequence chars) throws DecodingException {
        chars = this.trimTrailingPadding(chars);
        final byte[] tmp = new byte[this.maxDecodedSize(chars.length())];
        final int len = this.decodeTo(tmp, chars);
        return extract(tmp, len);
    }
    
    @GwtIncompatible
    public abstract InputStream decodingStream(final Reader p0);
    
    @GwtIncompatible
    public final ByteSource decodingSource(final CharSource encodedSource) {
        Preconditions.checkNotNull(encodedSource);
        return new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return BaseEncoding.this.decodingStream(encodedSource.openStream());
            }
        };
    }
    
    abstract int maxEncodedSize(final int p0);
    
    abstract void encodeTo(final Appendable p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    abstract int maxDecodedSize(final int p0);
    
    abstract int decodeTo(final byte[] p0, final CharSequence p1) throws DecodingException;
    
    CharSequence trimTrailingPadding(final CharSequence chars) {
        return Preconditions.checkNotNull(chars);
    }
    
    public abstract BaseEncoding omitPadding();
    
    public abstract BaseEncoding withPadChar(final char p0);
    
    public abstract BaseEncoding withSeparator(final String p0, final int p1);
    
    public abstract BaseEncoding upperCase();
    
    public abstract BaseEncoding lowerCase();
    
    public static BaseEncoding base64() {
        return BaseEncoding.BASE64;
    }
    
    public static BaseEncoding base64Url() {
        return BaseEncoding.BASE64_URL;
    }
    
    public static BaseEncoding base32() {
        return BaseEncoding.BASE32;
    }
    
    public static BaseEncoding base32Hex() {
        return BaseEncoding.BASE32_HEX;
    }
    
    public static BaseEncoding base16() {
        return BaseEncoding.BASE16;
    }
    
    @GwtIncompatible
    static Reader ignoringReader(final Reader delegate, final String toIgnore) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(toIgnore);
        return new Reader() {
            @Override
            public int read() throws IOException {
                int readChar;
                do {
                    readChar = delegate.read();
                } while (readChar != -1 && toIgnore.indexOf((char)readChar) >= 0);
                return readChar;
            }
            
            @Override
            public int read(final char[] cbuf, final int off, final int len) throws IOException {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void close() throws IOException {
                delegate.close();
            }
        };
    }
    
    static Appendable separatingAppendable(final Appendable delegate, final String separator, final int afterEveryChars) {
        Preconditions.checkNotNull(delegate);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(afterEveryChars > 0);
        return new Appendable() {
            int charsUntilSeparator = afterEveryChars;
            
            @Override
            public Appendable append(final char c) throws IOException {
                if (this.charsUntilSeparator == 0) {
                    delegate.append(separator);
                    this.charsUntilSeparator = afterEveryChars;
                }
                delegate.append(c);
                --this.charsUntilSeparator;
                return this;
            }
            
            @Override
            public Appendable append(@CheckForNull final CharSequence chars, final int off, final int len) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Appendable append(@CheckForNull final CharSequence chars) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    @GwtIncompatible
    static Writer separatingWriter(final Writer delegate, final String separator, final int afterEveryChars) {
        final Appendable separatingAppendable = separatingAppendable(delegate, separator, afterEveryChars);
        return new Writer() {
            @Override
            public void write(final int c) throws IOException {
                separatingAppendable.append((char)c);
            }
            
            @Override
            public void write(final char[] chars, final int off, final int len) throws IOException {
                throw new UnsupportedOperationException();
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
    
    static {
        BASE64 = new Base64Encoding("base64()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", '=');
        BASE64_URL = new Base64Encoding("base64Url()", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", '=');
        BASE32 = new StandardBaseEncoding("base32()", "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", '=');
        BASE32_HEX = new StandardBaseEncoding("base32Hex()", "0123456789ABCDEFGHIJKLMNOPQRSTUV", '=');
        BASE16 = new Base16Encoding("base16()", "0123456789ABCDEF");
    }
    
    public static final class DecodingException extends IOException
    {
        DecodingException(final String message) {
            super(message);
        }
        
        DecodingException(final Throwable cause) {
            super(cause);
        }
    }
    
    private static final class Alphabet
    {
        private final String name;
        private final char[] chars;
        final int mask;
        final int bitsPerChar;
        final int charsPerChunk;
        final int bytesPerChunk;
        private final byte[] decodabet;
        private final boolean[] validPadding;
        
        Alphabet(final String name, final char[] chars) {
            this.name = Preconditions.checkNotNull(name);
            this.chars = Preconditions.checkNotNull(chars);
            try {
                this.bitsPerChar = IntMath.log2(chars.length, RoundingMode.UNNECESSARY);
            }
            catch (ArithmeticException e) {
                throw new IllegalArgumentException(new StringBuilder(35).append("Illegal alphabet length ").append(chars.length).toString(), e);
            }
            final int gcd = Math.min(8, Integer.lowestOneBit(this.bitsPerChar));
            try {
                this.charsPerChunk = 8 / gcd;
                this.bytesPerChunk = this.bitsPerChar / gcd;
            }
            catch (ArithmeticException e2) {
                final String original = "Illegal alphabet ";
                final String value = String.valueOf(new String(chars));
                throw new IllegalArgumentException((value.length() != 0) ? original.concat(value) : new String(original), e2);
            }
            this.mask = chars.length - 1;
            final byte[] decodabet = new byte[128];
            Arrays.fill(decodabet, (byte)(-1));
            for (int i = 0; i < chars.length; ++i) {
                final char c = chars[i];
                Preconditions.checkArgument(c < decodabet.length, "Non-ASCII character: %s", c);
                Preconditions.checkArgument(decodabet[c] == -1, "Duplicate character: %s", c);
                decodabet[c] = (byte)i;
            }
            this.decodabet = decodabet;
            final boolean[] validPadding = new boolean[this.charsPerChunk];
            for (int j = 0; j < this.bytesPerChunk; ++j) {
                validPadding[IntMath.divide(j * 8, this.bitsPerChar, RoundingMode.CEILING)] = true;
            }
            this.validPadding = validPadding;
        }
        
        char encode(final int bits) {
            return this.chars[bits];
        }
        
        boolean isValidPaddingStartPosition(final int index) {
            return this.validPadding[index % this.charsPerChunk];
        }
        
        boolean canDecode(final char ch) {
            return ch <= '\u007f' && this.decodabet[ch] != -1;
        }
        
        int decode(final char ch) throws DecodingException {
            if (ch > '\u007f') {
                final String original = "Unrecognized character: 0x";
                final String value = String.valueOf(Integer.toHexString(ch));
                throw new DecodingException((value.length() != 0) ? original.concat(value) : new String(original));
            }
            final int result = this.decodabet[ch];
            if (result != -1) {
                return result;
            }
            if (ch <= ' ' || ch == '\u007f') {
                final String original2 = "Unrecognized character: 0x";
                final String value2 = String.valueOf(Integer.toHexString(ch));
                throw new DecodingException((value2.length() != 0) ? original2.concat(value2) : new String(original2));
            }
            throw new DecodingException(new StringBuilder(25).append("Unrecognized character: ").append(ch).toString());
        }
        
        private boolean hasLowerCase() {
            for (final char c : this.chars) {
                if (Ascii.isLowerCase(c)) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean hasUpperCase() {
            for (final char c : this.chars) {
                if (Ascii.isUpperCase(c)) {
                    return true;
                }
            }
            return false;
        }
        
        Alphabet upperCase() {
            if (!this.hasLowerCase()) {
                return this;
            }
            Preconditions.checkState(!this.hasUpperCase(), (Object)"Cannot call upperCase() on a mixed-case alphabet");
            final char[] upperCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; ++i) {
                upperCased[i] = Ascii.toUpperCase(this.chars[i]);
            }
            return new Alphabet(String.valueOf(this.name).concat(".upperCase()"), upperCased);
        }
        
        Alphabet lowerCase() {
            if (!this.hasUpperCase()) {
                return this;
            }
            Preconditions.checkState(!this.hasLowerCase(), (Object)"Cannot call lowerCase() on a mixed-case alphabet");
            final char[] lowerCased = new char[this.chars.length];
            for (int i = 0; i < this.chars.length; ++i) {
                lowerCased[i] = Ascii.toLowerCase(this.chars[i]);
            }
            return new Alphabet(String.valueOf(this.name).concat(".lowerCase()"), lowerCased);
        }
        
        public boolean matches(final char c) {
            return c < this.decodabet.length && this.decodabet[c] != -1;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public boolean equals(@CheckForNull final Object other) {
            if (other instanceof Alphabet) {
                final Alphabet that = (Alphabet)other;
                return Arrays.equals(this.chars, that.chars);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.chars);
        }
    }
    
    static class StandardBaseEncoding extends BaseEncoding
    {
        final Alphabet alphabet;
        @CheckForNull
        final Character paddingChar;
        @LazyInit
        @CheckForNull
        private transient BaseEncoding upperCase;
        @LazyInit
        @CheckForNull
        private transient BaseEncoding lowerCase;
        
        StandardBaseEncoding(final String name, final String alphabetChars, @CheckForNull final Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }
        
        StandardBaseEncoding(final Alphabet alphabet, @CheckForNull final Character paddingChar) {
            this.alphabet = Preconditions.checkNotNull(alphabet);
            Preconditions.checkArgument(paddingChar == null || !alphabet.matches(paddingChar), "Padding character %s was already in alphabet", paddingChar);
            this.paddingChar = paddingChar;
        }
        
        @Override
        int maxEncodedSize(final int bytes) {
            return this.alphabet.charsPerChunk * IntMath.divide(bytes, this.alphabet.bytesPerChunk, RoundingMode.CEILING);
        }
        
        @GwtIncompatible
        @Override
        public OutputStream encodingStream(final Writer out) {
            Preconditions.checkNotNull(out);
            return new OutputStream() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int writtenChars = 0;
                
                @Override
                public void write(final int b) throws IOException {
                    this.bitBuffer <<= 8;
                    this.bitBuffer |= (b & 0xFF);
                    this.bitBufferLength += 8;
                    while (this.bitBufferLength >= StandardBaseEncoding.this.alphabet.bitsPerChar) {
                        final int charIndex = this.bitBuffer >> this.bitBufferLength - StandardBaseEncoding.this.alphabet.bitsPerChar & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        ++this.writtenChars;
                        this.bitBufferLength -= StandardBaseEncoding.this.alphabet.bitsPerChar;
                    }
                }
                
                @Override
                public void flush() throws IOException {
                    out.flush();
                }
                
                @Override
                public void close() throws IOException {
                    if (this.bitBufferLength > 0) {
                        final int charIndex = this.bitBuffer << StandardBaseEncoding.this.alphabet.bitsPerChar - this.bitBufferLength & StandardBaseEncoding.this.alphabet.mask;
                        out.write(StandardBaseEncoding.this.alphabet.encode(charIndex));
                        ++this.writtenChars;
                        if (StandardBaseEncoding.this.paddingChar != null) {
                            while (this.writtenChars % StandardBaseEncoding.this.alphabet.charsPerChunk != 0) {
                                out.write(StandardBaseEncoding.this.paddingChar);
                                ++this.writtenChars;
                            }
                        }
                    }
                    out.close();
                }
            };
        }
        
        @Override
        void encodeTo(final Appendable target, final byte[] bytes, final int off, final int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            for (int i = 0; i < len; i += this.alphabet.bytesPerChunk) {
                this.encodeChunkTo(target, bytes, off + i, Math.min(this.alphabet.bytesPerChunk, len - i));
            }
        }
        
        void encodeChunkTo(final Appendable target, final byte[] bytes, final int off, final int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            Preconditions.checkArgument(len <= this.alphabet.bytesPerChunk);
            long bitBuffer = 0L;
            for (int i = 0; i < len; ++i) {
                bitBuffer |= (bytes[off + i] & 0xFF);
                bitBuffer <<= 8;
            }
            final int bitOffset = (len + 1) * 8 - this.alphabet.bitsPerChar;
            int bitsProcessed;
            for (bitsProcessed = 0; bitsProcessed < len * 8; bitsProcessed += this.alphabet.bitsPerChar) {
                final int charIndex = (int)(bitBuffer >>> bitOffset - bitsProcessed) & this.alphabet.mask;
                target.append(this.alphabet.encode(charIndex));
            }
            if (this.paddingChar != null) {
                while (bitsProcessed < this.alphabet.bytesPerChunk * 8) {
                    target.append(this.paddingChar);
                    bitsProcessed += this.alphabet.bitsPerChar;
                }
            }
        }
        
        @Override
        int maxDecodedSize(final int chars) {
            return (int)((this.alphabet.bitsPerChar * (long)chars + 7L) / 8L);
        }
        
        @Override
        CharSequence trimTrailingPadding(final CharSequence chars) {
            Preconditions.checkNotNull(chars);
            if (this.paddingChar == null) {
                return chars;
            }
            char padChar;
            int l;
            for (padChar = this.paddingChar, l = chars.length() - 1; l >= 0 && chars.charAt(l) == padChar; --l) {}
            return chars.subSequence(0, l + 1);
        }
        
        @Override
        public boolean canDecode(CharSequence chars) {
            Preconditions.checkNotNull(chars);
            chars = this.trimTrailingPadding(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
                return false;
            }
            for (int i = 0; i < chars.length(); ++i) {
                if (!this.alphabet.canDecode(chars.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        int decodeTo(final byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            chars = this.trimTrailingPadding(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(chars.length()).toString());
            }
            int bytesWritten = 0;
            for (int charIdx = 0; charIdx < chars.length(); charIdx += this.alphabet.charsPerChunk) {
                long chunk = 0L;
                int charsProcessed = 0;
                for (int i = 0; i < this.alphabet.charsPerChunk; ++i) {
                    chunk <<= this.alphabet.bitsPerChar;
                    if (charIdx + i < chars.length()) {
                        chunk |= this.alphabet.decode(chars.charAt(charIdx + charsProcessed++));
                    }
                }
                for (int minOffset = this.alphabet.bytesPerChunk * 8 - charsProcessed * this.alphabet.bitsPerChar, offset = (this.alphabet.bytesPerChunk - 1) * 8; offset >= minOffset; offset -= 8) {
                    target[bytesWritten++] = (byte)(chunk >>> offset & 0xFFL);
                }
            }
            return bytesWritten;
        }
        
        @GwtIncompatible
        @Override
        public InputStream decodingStream(final Reader reader) {
            Preconditions.checkNotNull(reader);
            return new InputStream() {
                int bitBuffer = 0;
                int bitBufferLength = 0;
                int readChars = 0;
                boolean hitPadding = false;
                
                @Override
                public int read() throws IOException {
                    while (true) {
                        final int readChar = reader.read();
                        if (readChar == -1) {
                            if (!this.hitPadding && !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars)) {
                                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(this.readChars).toString());
                            }
                            return -1;
                        }
                        else {
                            ++this.readChars;
                            final char ch = (char)readChar;
                            if (StandardBaseEncoding.this.paddingChar != null && StandardBaseEncoding.this.paddingChar == ch) {
                                if (!this.hitPadding && (this.readChars == 1 || !StandardBaseEncoding.this.alphabet.isValidPaddingStartPosition(this.readChars - 1))) {
                                    throw new DecodingException(new StringBuilder(41).append("Padding cannot start at index ").append(this.readChars).toString());
                                }
                                this.hitPadding = true;
                            }
                            else {
                                if (this.hitPadding) {
                                    throw new DecodingException(new StringBuilder(61).append("Expected padding character but found '").append(ch).append("' at index ").append(this.readChars).toString());
                                }
                                this.bitBuffer <<= StandardBaseEncoding.this.alphabet.bitsPerChar;
                                this.bitBuffer |= StandardBaseEncoding.this.alphabet.decode(ch);
                                this.bitBufferLength += StandardBaseEncoding.this.alphabet.bitsPerChar;
                                if (this.bitBufferLength >= 8) {
                                    this.bitBufferLength -= 8;
                                    return this.bitBuffer >> this.bitBufferLength & 0xFF;
                                }
                                continue;
                            }
                        }
                    }
                }
                
                @Override
                public int read(final byte[] buf, final int off, final int len) throws IOException {
                    Preconditions.checkPositionIndexes(off, off + len, buf.length);
                    int i;
                    for (i = off; i < off + len; ++i) {
                        final int b = this.read();
                        if (b == -1) {
                            final int read = i - off;
                            return (read == 0) ? -1 : read;
                        }
                        buf[i] = (byte)b;
                    }
                    return i - off;
                }
                
                @Override
                public void close() throws IOException {
                    reader.close();
                }
            };
        }
        
        @Override
        public BaseEncoding omitPadding() {
            return (this.paddingChar == null) ? this : this.newInstance(this.alphabet, null);
        }
        
        @Override
        public BaseEncoding withPadChar(final char padChar) {
            if (8 % this.alphabet.bitsPerChar == 0 || (this.paddingChar != null && this.paddingChar == padChar)) {
                return this;
            }
            return this.newInstance(this.alphabet, padChar);
        }
        
        @Override
        public BaseEncoding withSeparator(final String separator, final int afterEveryChars) {
            for (int i = 0; i < separator.length(); ++i) {
                Preconditions.checkArgument(!this.alphabet.matches(separator.charAt(i)), "Separator (%s) cannot contain alphabet characters", separator);
            }
            if (this.paddingChar != null) {
                Preconditions.checkArgument(separator.indexOf(this.paddingChar) < 0, "Separator (%s) cannot contain padding character", separator);
            }
            return new SeparatedBaseEncoding(this, separator, afterEveryChars);
        }
        
        @Override
        public BaseEncoding upperCase() {
            BaseEncoding result = this.upperCase;
            if (result == null) {
                final Alphabet upper = this.alphabet.upperCase();
                BaseEncoding baseEncoding;
                StandardBaseEncoding instance;
                if (upper == this.alphabet) {
                    baseEncoding = this;
                    instance = this;
                }
                else {
                    baseEncoding = (instance = (StandardBaseEncoding)this.newInstance(upper, this.paddingChar));
                }
                this.upperCase = instance;
                result = baseEncoding;
            }
            return result;
        }
        
        @Override
        public BaseEncoding lowerCase() {
            BaseEncoding result = this.lowerCase;
            if (result == null) {
                final Alphabet lower = this.alphabet.lowerCase();
                BaseEncoding baseEncoding;
                StandardBaseEncoding instance;
                if (lower == this.alphabet) {
                    baseEncoding = this;
                    instance = this;
                }
                else {
                    baseEncoding = (instance = (StandardBaseEncoding)this.newInstance(lower, this.paddingChar));
                }
                this.lowerCase = instance;
                result = baseEncoding;
            }
            return result;
        }
        
        BaseEncoding newInstance(final Alphabet alphabet, @CheckForNull final Character paddingChar) {
            return new StandardBaseEncoding(alphabet, paddingChar);
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder("BaseEncoding.");
            builder.append(this.alphabet.toString());
            if (8 % this.alphabet.bitsPerChar != 0) {
                if (this.paddingChar == null) {
                    builder.append(".omitPadding()");
                }
                else {
                    builder.append(".withPadChar('").append(this.paddingChar).append("')");
                }
            }
            return builder.toString();
        }
        
        @Override
        public boolean equals(@CheckForNull final Object other) {
            if (other instanceof StandardBaseEncoding) {
                final StandardBaseEncoding that = (StandardBaseEncoding)other;
                return this.alphabet.equals(that.alphabet) && Objects.equal(this.paddingChar, that.paddingChar);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.alphabet.hashCode() ^ Objects.hashCode(this.paddingChar);
        }
    }
    
    static final class Base16Encoding extends StandardBaseEncoding
    {
        final char[] encoding;
        
        Base16Encoding(final String name, final String alphabetChars) {
            this(new Alphabet(name, alphabetChars.toCharArray()));
        }
        
        private Base16Encoding(final Alphabet alphabet) {
            super(alphabet, null);
            this.encoding = new char[512];
            Preconditions.checkArgument(alphabet.chars.length == 16);
            for (int i = 0; i < 256; ++i) {
                this.encoding[i] = alphabet.encode(i >>> 4);
                this.encoding[i | 0x100] = alphabet.encode(i & 0xF);
            }
        }
        
        @Override
        void encodeTo(final Appendable target, final byte[] bytes, final int off, final int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            for (int i = 0; i < len; ++i) {
                final int b = bytes[off + i] & 0xFF;
                target.append(this.encoding[b]);
                target.append(this.encoding[b | 0x100]);
            }
        }
        
        @Override
        int decodeTo(final byte[] target, final CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            if (chars.length() % 2 == 1) {
                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(chars.length()).toString());
            }
            int bytesWritten = 0;
            for (int i = 0; i < chars.length(); i += 2) {
                final int decoded = this.alphabet.decode(chars.charAt(i)) << 4 | this.alphabet.decode(chars.charAt(i + 1));
                target[bytesWritten++] = (byte)decoded;
            }
            return bytesWritten;
        }
        
        @Override
        BaseEncoding newInstance(final Alphabet alphabet, @CheckForNull final Character paddingChar) {
            return new Base16Encoding(alphabet);
        }
    }
    
    static final class Base64Encoding extends StandardBaseEncoding
    {
        Base64Encoding(final String name, final String alphabetChars, @CheckForNull final Character paddingChar) {
            this(new Alphabet(name, alphabetChars.toCharArray()), paddingChar);
        }
        
        private Base64Encoding(final Alphabet alphabet, @CheckForNull final Character paddingChar) {
            super(alphabet, paddingChar);
            Preconditions.checkArgument(alphabet.chars.length == 64);
        }
        
        @Override
        void encodeTo(final Appendable target, final byte[] bytes, final int off, final int len) throws IOException {
            Preconditions.checkNotNull(target);
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            int i = off;
            for (int remaining = len; remaining >= 3; remaining -= 3) {
                final int chunk = (bytes[i++] & 0xFF) << 16 | (bytes[i++] & 0xFF) << 8 | (bytes[i++] & 0xFF);
                target.append(this.alphabet.encode(chunk >>> 18));
                target.append(this.alphabet.encode(chunk >>> 12 & 0x3F));
                target.append(this.alphabet.encode(chunk >>> 6 & 0x3F));
                target.append(this.alphabet.encode(chunk & 0x3F));
            }
            if (i < off + len) {
                this.encodeChunkTo(target, bytes, i, off + len - i);
            }
        }
        
        @Override
        int decodeTo(final byte[] target, CharSequence chars) throws DecodingException {
            Preconditions.checkNotNull(target);
            chars = this.trimTrailingPadding(chars);
            if (!this.alphabet.isValidPaddingStartPosition(chars.length())) {
                throw new DecodingException(new StringBuilder(32).append("Invalid input length ").append(chars.length()).toString());
            }
            int bytesWritten = 0;
            int i = 0;
            while (i < chars.length()) {
                int chunk = this.alphabet.decode(chars.charAt(i++)) << 18;
                chunk |= this.alphabet.decode(chars.charAt(i++)) << 12;
                target[bytesWritten++] = (byte)(chunk >>> 16);
                if (i < chars.length()) {
                    chunk |= this.alphabet.decode(chars.charAt(i++)) << 6;
                    target[bytesWritten++] = (byte)(chunk >>> 8 & 0xFF);
                    if (i >= chars.length()) {
                        continue;
                    }
                    chunk |= this.alphabet.decode(chars.charAt(i++));
                    target[bytesWritten++] = (byte)(chunk & 0xFF);
                }
            }
            return bytesWritten;
        }
        
        @Override
        BaseEncoding newInstance(final Alphabet alphabet, @CheckForNull final Character paddingChar) {
            return new Base64Encoding(alphabet, paddingChar);
        }
    }
    
    static final class SeparatedBaseEncoding extends BaseEncoding
    {
        private final BaseEncoding delegate;
        private final String separator;
        private final int afterEveryChars;
        
        SeparatedBaseEncoding(final BaseEncoding delegate, final String separator, final int afterEveryChars) {
            this.delegate = Preconditions.checkNotNull(delegate);
            this.separator = Preconditions.checkNotNull(separator);
            this.afterEveryChars = afterEveryChars;
            Preconditions.checkArgument(afterEveryChars > 0, "Cannot add a separator after every %s chars", afterEveryChars);
        }
        
        @Override
        CharSequence trimTrailingPadding(final CharSequence chars) {
            return this.delegate.trimTrailingPadding(chars);
        }
        
        @Override
        int maxEncodedSize(final int bytes) {
            final int unseparatedSize = this.delegate.maxEncodedSize(bytes);
            return unseparatedSize + this.separator.length() * IntMath.divide(Math.max(0, unseparatedSize - 1), this.afterEveryChars, RoundingMode.FLOOR);
        }
        
        @GwtIncompatible
        @Override
        public OutputStream encodingStream(final Writer output) {
            return this.delegate.encodingStream(BaseEncoding.separatingWriter(output, this.separator, this.afterEveryChars));
        }
        
        @Override
        void encodeTo(final Appendable target, final byte[] bytes, final int off, final int len) throws IOException {
            this.delegate.encodeTo(BaseEncoding.separatingAppendable(target, this.separator, this.afterEveryChars), bytes, off, len);
        }
        
        @Override
        int maxDecodedSize(final int chars) {
            return this.delegate.maxDecodedSize(chars);
        }
        
        @Override
        public boolean canDecode(final CharSequence chars) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < chars.length(); ++i) {
                final char c = chars.charAt(i);
                if (this.separator.indexOf(c) < 0) {
                    builder.append(c);
                }
            }
            return this.delegate.canDecode(builder);
        }
        
        @Override
        int decodeTo(final byte[] target, final CharSequence chars) throws DecodingException {
            final StringBuilder stripped = new StringBuilder(chars.length());
            for (int i = 0; i < chars.length(); ++i) {
                final char c = chars.charAt(i);
                if (this.separator.indexOf(c) < 0) {
                    stripped.append(c);
                }
            }
            return this.delegate.decodeTo(target, stripped);
        }
        
        @GwtIncompatible
        @Override
        public InputStream decodingStream(final Reader reader) {
            return this.delegate.decodingStream(BaseEncoding.ignoringReader(reader, this.separator));
        }
        
        @Override
        public BaseEncoding omitPadding() {
            return this.delegate.omitPadding().withSeparator(this.separator, this.afterEveryChars);
        }
        
        @Override
        public BaseEncoding withPadChar(final char padChar) {
            return this.delegate.withPadChar(padChar).withSeparator(this.separator, this.afterEveryChars);
        }
        
        @Override
        public BaseEncoding withSeparator(final String separator, final int afterEveryChars) {
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
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.delegate);
            final String separator = this.separator;
            return new StringBuilder(31 + String.valueOf(value).length() + String.valueOf(separator).length()).append(value).append(".withSeparator(\"").append(separator).append("\", ").append(this.afterEveryChars).append(")").toString();
        }
    }
}
