/*
 * Decompiled with CFR 0.150.
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
        for (int i = 0; i < len; ++i) {
            hasher.putChar(input.charAt(i));
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
        public Hasher putByte(byte b) {
            this.stream.write(b);
            return this;
        }

        @Override
        public Hasher putBytes(byte[] bytes) {
            try {
                this.stream.write(bytes);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        @Override
        public Hasher putBytes(byte[] bytes, int off, int len) {
            this.stream.write(bytes, off, len);
            return this;
        }

        @Override
        public Hasher putShort(short s) {
            this.stream.write(s & 0xFF);
            this.stream.write(s >>> 8 & 0xFF);
            return this;
        }

        @Override
        public Hasher putInt(int i) {
            this.stream.write(i & 0xFF);
            this.stream.write(i >>> 8 & 0xFF);
            this.stream.write(i >>> 16 & 0xFF);
            this.stream.write(i >>> 24 & 0xFF);
            return this;
        }

        @Override
        public Hasher putLong(long l) {
            for (int i = 0; i < 64; i += 8) {
                this.stream.write((byte)(l >>> i & 0xFFL));
            }
            return this;
        }

        @Override
        public Hasher putChar(char c) {
            this.stream.write(c & 0xFF);
            this.stream.write(c >>> 8 & 0xFF);
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

