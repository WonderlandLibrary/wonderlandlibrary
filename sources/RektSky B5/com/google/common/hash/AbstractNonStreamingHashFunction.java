/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.hash.AbstractHasher;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

abstract class AbstractNonStreamingHashFunction
implements HashFunction {
    AbstractNonStreamingHashFunction() {
    }

    @Override
    public Hasher newHasher() {
        return new BufferingHasher(32);
    }

    @Override
    public Hasher newHasher(int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return new BufferingHasher(expectedInputSize);
    }

    @Override
    public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
        return this.newHasher().putObject(instance, funnel).hash();
    }

    @Override
    public HashCode hashUnencodedChars(CharSequence input) {
        int len = input.length();
        Hasher hasher = this.newHasher(len * 2);
        for (int i2 = 0; i2 < len; ++i2) {
            hasher.putChar(input.charAt(i2));
        }
        return hasher.hash();
    }

    @Override
    public HashCode hashString(CharSequence input, Charset charset) {
        return this.hashBytes(input.toString().getBytes(charset));
    }

    @Override
    public HashCode hashInt(int input) {
        return this.newHasher(4).putInt(input).hash();
    }

    @Override
    public HashCode hashLong(long input) {
        return this.newHasher(8).putLong(input).hash();
    }

    @Override
    public HashCode hashBytes(byte[] input) {
        return this.hashBytes(input, 0, input.length);
    }

    private static final class ExposedByteArrayOutputStream
    extends ByteArrayOutputStream {
        ExposedByteArrayOutputStream(int expectedInputSize) {
            super(expectedInputSize);
        }

        byte[] byteArray() {
            return this.buf;
        }

        int length() {
            return this.count;
        }
    }

    private final class BufferingHasher
    extends AbstractHasher {
        final ExposedByteArrayOutputStream stream;
        static final int BOTTOM_BYTE = 255;

        BufferingHasher(int expectedInputSize) {
            this.stream = new ExposedByteArrayOutputStream(expectedInputSize);
        }

        @Override
        public Hasher putByte(byte b2) {
            this.stream.write(b2);
            return this;
        }

        @Override
        public Hasher putBytes(byte[] bytes) {
            try {
                this.stream.write(bytes);
            }
            catch (IOException e2) {
                throw new RuntimeException(e2);
            }
            return this;
        }

        @Override
        public Hasher putBytes(byte[] bytes, int off, int len) {
            this.stream.write(bytes, off, len);
            return this;
        }

        @Override
        public Hasher putShort(short s2) {
            this.stream.write(s2 & 0xFF);
            this.stream.write(s2 >>> 8 & 0xFF);
            return this;
        }

        @Override
        public Hasher putInt(int i2) {
            this.stream.write(i2 & 0xFF);
            this.stream.write(i2 >>> 8 & 0xFF);
            this.stream.write(i2 >>> 16 & 0xFF);
            this.stream.write(i2 >>> 24 & 0xFF);
            return this;
        }

        @Override
        public Hasher putLong(long l2) {
            for (int i2 = 0; i2 < 64; i2 += 8) {
                this.stream.write((byte)(l2 >>> i2 & 0xFFL));
            }
            return this;
        }

        @Override
        public Hasher putChar(char c2) {
            this.stream.write(c2 & 0xFF);
            this.stream.write(c2 >>> 8 & 0xFF);
            return this;
        }

        @Override
        public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
            funnel.funnel(instance, this);
            return this;
        }

        @Override
        public HashCode hash() {
            return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
        }
    }
}

