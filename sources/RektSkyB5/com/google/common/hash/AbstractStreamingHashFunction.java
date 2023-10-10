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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

abstract class AbstractStreamingHashFunction
implements HashFunction {
    AbstractStreamingHashFunction() {
    }

    @Override
    public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
        return this.newHasher().putObject(instance, funnel).hash();
    }

    @Override
    public HashCode hashUnencodedChars(CharSequence input) {
        return this.newHasher().putUnencodedChars(input).hash();
    }

    @Override
    public HashCode hashString(CharSequence input, Charset charset) {
        return this.newHasher().putString(input, charset).hash();
    }

    @Override
    public HashCode hashInt(int input) {
        return this.newHasher().putInt(input).hash();
    }

    @Override
    public HashCode hashLong(long input) {
        return this.newHasher().putLong(input).hash();
    }

    @Override
    public HashCode hashBytes(byte[] input) {
        return this.newHasher().putBytes(input).hash();
    }

    @Override
    public HashCode hashBytes(byte[] input, int off, int len) {
        return this.newHasher().putBytes(input, off, len).hash();
    }

    @Override
    public Hasher newHasher(int expectedInputSize) {
        Preconditions.checkArgument(expectedInputSize >= 0);
        return this.newHasher();
    }

    protected static abstract class AbstractStreamingHasher
    extends AbstractHasher {
        private final ByteBuffer buffer;
        private final int bufferSize;
        private final int chunkSize;

        protected AbstractStreamingHasher(int chunkSize) {
            this(chunkSize, chunkSize);
        }

        protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
            Preconditions.checkArgument(bufferSize % chunkSize == 0);
            this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
            this.bufferSize = bufferSize;
            this.chunkSize = chunkSize;
        }

        protected abstract void process(ByteBuffer var1);

        protected void processRemaining(ByteBuffer bb) {
            bb.position(bb.limit());
            bb.limit(this.chunkSize + 7);
            while (bb.position() < this.chunkSize) {
                bb.putLong(0L);
            }
            bb.limit(this.chunkSize);
            bb.flip();
            this.process(bb);
        }

        @Override
        public final Hasher putBytes(byte[] bytes) {
            return this.putBytes(bytes, 0, bytes.length);
        }

        @Override
        public final Hasher putBytes(byte[] bytes, int off, int len) {
            return this.putBytes(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
        }

        private Hasher putBytes(ByteBuffer readBuffer) {
            if (readBuffer.remaining() <= this.buffer.remaining()) {
                this.buffer.put(readBuffer);
                this.munchIfFull();
                return this;
            }
            int bytesToCopy = this.bufferSize - this.buffer.position();
            for (int i2 = 0; i2 < bytesToCopy; ++i2) {
                this.buffer.put(readBuffer.get());
            }
            this.munch();
            while (readBuffer.remaining() >= this.chunkSize) {
                this.process(readBuffer);
            }
            this.buffer.put(readBuffer);
            return this;
        }

        @Override
        public final Hasher putUnencodedChars(CharSequence charSequence) {
            for (int i2 = 0; i2 < charSequence.length(); ++i2) {
                this.putChar(charSequence.charAt(i2));
            }
            return this;
        }

        @Override
        public final Hasher putByte(byte b2) {
            this.buffer.put(b2);
            this.munchIfFull();
            return this;
        }

        @Override
        public final Hasher putShort(short s2) {
            this.buffer.putShort(s2);
            this.munchIfFull();
            return this;
        }

        @Override
        public final Hasher putChar(char c2) {
            this.buffer.putChar(c2);
            this.munchIfFull();
            return this;
        }

        @Override
        public final Hasher putInt(int i2) {
            this.buffer.putInt(i2);
            this.munchIfFull();
            return this;
        }

        @Override
        public final Hasher putLong(long l2) {
            this.buffer.putLong(l2);
            this.munchIfFull();
            return this;
        }

        @Override
        public final <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
            funnel.funnel(instance, this);
            return this;
        }

        @Override
        public final HashCode hash() {
            this.munch();
            this.buffer.flip();
            if (this.buffer.remaining() > 0) {
                this.processRemaining(this.buffer);
            }
            return this.makeHash();
        }

        abstract HashCode makeHash();

        private void munchIfFull() {
            if (this.buffer.remaining() < 8) {
                this.munch();
            }
        }

        private void munch() {
            this.buffer.flip();
            while (this.buffer.remaining() >= this.chunkSize) {
                this.process(this.buffer);
            }
            this.buffer.compact();
        }
    }
}

