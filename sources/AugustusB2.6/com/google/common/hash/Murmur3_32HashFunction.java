// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedBytes;
import com.google.common.base.Preconditions;
import com.google.common.base.Charsets;
import java.nio.charset.Charset;
import javax.annotation.CheckForNull;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;

@Immutable
@ElementTypesAreNonnullByDefault
final class Murmur3_32HashFunction extends AbstractHashFunction implements Serializable
{
    static final HashFunction MURMUR3_32;
    static final HashFunction MURMUR3_32_FIXED;
    static final HashFunction GOOD_FAST_HASH_32;
    private static final int CHUNK_SIZE = 4;
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private final int seed;
    private final boolean supplementaryPlaneFix;
    private static final long serialVersionUID = 0L;
    
    Murmur3_32HashFunction(final int seed, final boolean supplementaryPlaneFix) {
        this.seed = seed;
        this.supplementaryPlaneFix = supplementaryPlaneFix;
    }
    
    @Override
    public int bits() {
        return 32;
    }
    
    @Override
    public Hasher newHasher() {
        return new Murmur3_32Hasher(this.seed);
    }
    
    @Override
    public String toString() {
        return new StringBuilder(31).append("Hashing.murmur3_32(").append(this.seed).append(")").toString();
    }
    
    @Override
    public boolean equals(@CheckForNull final Object object) {
        if (object instanceof Murmur3_32HashFunction) {
            final Murmur3_32HashFunction other = (Murmur3_32HashFunction)object;
            return this.seed == other.seed && this.supplementaryPlaneFix == other.supplementaryPlaneFix;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ this.seed;
    }
    
    @Override
    public HashCode hashInt(final int input) {
        final int k1 = mixK1(input);
        final int h1 = mixH1(this.seed, k1);
        return fmix(h1, 4);
    }
    
    @Override
    public HashCode hashLong(final long input) {
        final int low = (int)input;
        final int high = (int)(input >>> 32);
        int k1 = mixK1(low);
        int h1 = mixH1(this.seed, k1);
        k1 = mixK1(high);
        h1 = mixH1(h1, k1);
        return fmix(h1, 8);
    }
    
    @Override
    public HashCode hashUnencodedChars(final CharSequence input) {
        int h1 = this.seed;
        for (int i = 1; i < input.length(); i += 2) {
            int k1 = input.charAt(i - 1) | input.charAt(i) << 16;
            k1 = mixK1(k1);
            h1 = mixH1(h1, k1);
        }
        if ((input.length() & 0x1) == 0x1) {
            int k2 = input.charAt(input.length() - 1);
            k2 = mixK1(k2);
            h1 ^= k2;
        }
        return fmix(h1, 2 * input.length());
    }
    
    @Override
    public HashCode hashString(final CharSequence input, final Charset charset) {
        if (Charsets.UTF_8.equals(charset)) {
            final int utf16Length = input.length();
            int h1 = this.seed;
            int i;
            int len;
            for (i = 0, len = 0; i + 4 <= utf16Length; i += 4, len += 4) {
                final char c0 = input.charAt(i);
                final char c2 = input.charAt(i + 1);
                final char c3 = input.charAt(i + 2);
                final char c4 = input.charAt(i + 3);
                if (c0 >= '\u0080' || c2 >= '\u0080' || c3 >= '\u0080' || c4 >= '\u0080') {
                    break;
                }
                int k1 = c0 | c2 << 8 | c3 << 16 | c4 << 24;
                k1 = mixK1(k1);
                h1 = mixH1(h1, k1);
            }
            long buffer = 0L;
            int shift = 0;
            while (i < utf16Length) {
                final char c5 = input.charAt(i);
                if (c5 < '\u0080') {
                    buffer |= (long)c5 << shift;
                    shift += 8;
                    ++len;
                }
                else if (c5 < '\u0800') {
                    buffer |= charToTwoUtf8Bytes(c5) << shift;
                    shift += 16;
                    len += 2;
                }
                else if (c5 < '\ud800' || c5 > '\udfff') {
                    buffer |= charToThreeUtf8Bytes(c5) << shift;
                    shift += 24;
                    len += 3;
                }
                else {
                    final int codePoint = Character.codePointAt(input, i);
                    if (codePoint == c5) {
                        return this.hashBytes(input.toString().getBytes(charset));
                    }
                    ++i;
                    buffer |= codePointToFourUtf8Bytes(codePoint) << shift;
                    if (this.supplementaryPlaneFix) {
                        shift += 32;
                    }
                    len += 4;
                }
                if (shift >= 32) {
                    final int k1 = mixK1((int)buffer);
                    h1 = mixH1(h1, k1);
                    buffer >>>= 32;
                    shift -= 32;
                }
                ++i;
            }
            final int k2 = mixK1((int)buffer);
            h1 ^= k2;
            return fmix(h1, len);
        }
        return this.hashBytes(input.toString().getBytes(charset));
    }
    
    @Override
    public HashCode hashBytes(final byte[] input, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        int h1 = this.seed;
        int i;
        for (i = 0; i + 4 <= len; i += 4) {
            final int k1 = mixK1(getIntLittleEndian(input, off + i));
            h1 = mixH1(h1, k1);
        }
        int k1 = 0;
        for (int shift = 0; i < len; ++i, shift += 8) {
            k1 ^= UnsignedBytes.toInt(input[off + i]) << shift;
        }
        h1 ^= mixK1(k1);
        return fmix(h1, len);
    }
    
    private static int getIntLittleEndian(final byte[] input, final int offset) {
        return Ints.fromBytes(input[offset + 3], input[offset + 2], input[offset + 1], input[offset]);
    }
    
    private static int mixK1(int k1) {
        k1 *= -862048943;
        k1 = Integer.rotateLeft(k1, 15);
        k1 *= 461845907;
        return k1;
    }
    
    private static int mixH1(int h1, final int k1) {
        h1 ^= k1;
        h1 = Integer.rotateLeft(h1, 13);
        h1 = h1 * 5 - 430675100;
        return h1;
    }
    
    private static HashCode fmix(int h1, final int length) {
        h1 ^= length;
        h1 ^= h1 >>> 16;
        h1 *= -2048144789;
        h1 ^= h1 >>> 13;
        h1 *= -1028477387;
        h1 ^= h1 >>> 16;
        return HashCode.fromInt(h1);
    }
    
    private static long codePointToFourUtf8Bytes(final int codePoint) {
        return 0xF0L | (long)(codePoint >>> 18) | (0x80L | (long)(0x3F & codePoint >>> 12)) << 8 | (0x80L | (long)(0x3F & codePoint >>> 6)) << 16 | (0x80L | (long)(0x3F & codePoint)) << 24;
    }
    
    private static long charToThreeUtf8Bytes(final char c) {
        return 0xE0L | (long)(c >>> 12) | (long)((0x80 | (0x3F & c >>> 6)) << 8) | (long)((0x80 | ('?' & c)) << 16);
    }
    
    private static long charToTwoUtf8Bytes(final char c) {
        return 0xC0L | (long)(c >>> 6) | (long)((0x80 | ('?' & c)) << 8);
    }
    
    static {
        MURMUR3_32 = new Murmur3_32HashFunction(0, false);
        MURMUR3_32_FIXED = new Murmur3_32HashFunction(0, true);
        GOOD_FAST_HASH_32 = new Murmur3_32HashFunction(Hashing.GOOD_FAST_HASH_SEED, true);
    }
    
    @CanIgnoreReturnValue
    private static final class Murmur3_32Hasher extends AbstractHasher
    {
        private int h1;
        private long buffer;
        private int shift;
        private int length;
        private boolean isDone;
        
        Murmur3_32Hasher(final int seed) {
            this.h1 = seed;
            this.length = 0;
            this.isDone = false;
        }
        
        private void update(final int nBytes, final long update) {
            this.buffer |= (update & 0xFFFFFFFFL) << this.shift;
            this.shift += nBytes * 8;
            this.length += nBytes;
            if (this.shift >= 32) {
                this.h1 = mixH1(this.h1, mixK1((int)this.buffer));
                this.buffer >>>= 32;
                this.shift -= 32;
            }
        }
        
        @Override
        public Hasher putByte(final byte b) {
            this.update(1, b & 0xFF);
            return this;
        }
        
        @Override
        public Hasher putBytes(final byte[] bytes, final int off, final int len) {
            Preconditions.checkPositionIndexes(off, off + len, bytes.length);
            int i;
            for (i = 0; i + 4 <= len; i += 4) {
                this.update(4, getIntLittleEndian(bytes, off + i));
            }
            while (i < len) {
                this.putByte(bytes[off + i]);
                ++i;
            }
            return this;
        }
        
        @Override
        public Hasher putBytes(final ByteBuffer buffer) {
            final ByteOrder bo = buffer.order();
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            while (buffer.remaining() >= 4) {
                this.putInt(buffer.getInt());
            }
            while (buffer.hasRemaining()) {
                this.putByte(buffer.get());
            }
            buffer.order(bo);
            return this;
        }
        
        @Override
        public Hasher putInt(final int i) {
            this.update(4, i);
            return this;
        }
        
        @Override
        public Hasher putLong(final long l) {
            this.update(4, (int)l);
            this.update(4, l >>> 32);
            return this;
        }
        
        @Override
        public Hasher putChar(final char c) {
            this.update(2, c);
            return this;
        }
        
        @Override
        public Hasher putString(final CharSequence input, final Charset charset) {
            if (Charsets.UTF_8.equals(charset)) {
                int utf16Length;
                int i;
                for (utf16Length = input.length(), i = 0; i + 4 <= utf16Length; i += 4) {
                    final char c0 = input.charAt(i);
                    final char c2 = input.charAt(i + 1);
                    final char c3 = input.charAt(i + 2);
                    final char c4 = input.charAt(i + 3);
                    if (c0 >= '\u0080' || c2 >= '\u0080' || c3 >= '\u0080' || c4 >= '\u0080') {
                        break;
                    }
                    this.update(4, c0 | c2 << 8 | c3 << 16 | c4 << 24);
                }
                while (i < utf16Length) {
                    final char c5 = input.charAt(i);
                    if (c5 < '\u0080') {
                        this.update(1, c5);
                    }
                    else if (c5 < '\u0800') {
                        this.update(2, charToTwoUtf8Bytes(c5));
                    }
                    else if (c5 < '\ud800' || c5 > '\udfff') {
                        this.update(3, charToThreeUtf8Bytes(c5));
                    }
                    else {
                        final int codePoint = Character.codePointAt(input, i);
                        if (codePoint == c5) {
                            this.putBytes(input.subSequence(i, utf16Length).toString().getBytes(charset));
                            return this;
                        }
                        ++i;
                        this.update(4, codePointToFourUtf8Bytes(codePoint));
                    }
                    ++i;
                }
                return this;
            }
            return super.putString(input, charset);
        }
        
        @Override
        public HashCode hash() {
            Preconditions.checkState(!this.isDone);
            this.isDone = true;
            this.h1 ^= mixK1((int)this.buffer);
            return fmix(this.h1, this.length);
        }
    }
}
