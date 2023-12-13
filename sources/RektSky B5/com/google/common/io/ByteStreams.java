/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

@Beta
public final class ByteStreams {
    private static final int BUF_SIZE = 4096;
    private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream(){

        @Override
        public void write(int b2) {
        }

        @Override
        public void write(byte[] b2) {
            Preconditions.checkNotNull(b2);
        }

        @Override
        public void write(byte[] b2, int off, int len) {
            Preconditions.checkNotNull(b2);
        }

        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }
    };

    private ByteStreams() {
    }

    @Deprecated
    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] b2) {
        return ByteStreams.asInputSupplier(ByteSource.wrap(b2));
    }

    @Deprecated
    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] b2, int off, int len) {
        return ByteStreams.asInputSupplier(ByteSource.wrap(b2).slice(off, len));
    }

    @Deprecated
    public static void write(byte[] from, OutputSupplier<? extends OutputStream> to) throws IOException {
        ByteStreams.asByteSink(to).write(from);
    }

    @Deprecated
    public static long copy(InputSupplier<? extends InputStream> from, OutputSupplier<? extends OutputStream> to) throws IOException {
        return ByteStreams.asByteSource(from).copyTo(ByteStreams.asByteSink(to));
    }

    @Deprecated
    public static long copy(InputSupplier<? extends InputStream> from, OutputStream to) throws IOException {
        return ByteStreams.asByteSource(from).copyTo(to);
    }

    @Deprecated
    public static long copy(InputStream from, OutputSupplier<? extends OutputStream> to) throws IOException {
        return ByteStreams.asByteSink(to).writeFrom(from);
    }

    public static long copy(InputStream from, OutputStream to) throws IOException {
        int r2;
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        byte[] buf = new byte[4096];
        long total = 0L;
        while ((r2 = from.read(buf)) != -1) {
            to.write(buf, 0, r2);
            total += (long)r2;
        }
        return total;
    }

    public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        ByteBuffer buf = ByteBuffer.allocate(4096);
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                total += (long)to.write(buf);
            }
            buf.clear();
        }
        return total;
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteStreams.copy(in, (OutputStream)out);
        return out.toByteArray();
    }

    static byte[] toByteArray(InputStream in, int expectedSize) throws IOException {
        int read;
        byte[] bytes = new byte[expectedSize];
        for (int remaining = expectedSize; remaining > 0; remaining -= read) {
            int off = expectedSize - remaining;
            read = in.read(bytes, off, remaining);
            if (read != -1) continue;
            return Arrays.copyOf(bytes, off);
        }
        int b2 = in.read();
        if (b2 == -1) {
            return bytes;
        }
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write(b2);
        ByteStreams.copy(in, (OutputStream)out);
        byte[] result = new byte[bytes.length + out.size()];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        out.writeTo(result, bytes.length);
        return result;
    }

    @Deprecated
    public static byte[] toByteArray(InputSupplier<? extends InputStream> supplier) throws IOException {
        return ByteStreams.asByteSource(supplier).read();
    }

    public static ByteArrayDataInput newDataInput(byte[] bytes) {
        return ByteStreams.newDataInput(new ByteArrayInputStream(bytes));
    }

    public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
        Preconditions.checkPositionIndex(start, bytes.length);
        return ByteStreams.newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
    }

    public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
        return new ByteArrayDataInputStream(Preconditions.checkNotNull(byteArrayInputStream));
    }

    public static ByteArrayDataOutput newDataOutput() {
        return ByteStreams.newDataOutput(new ByteArrayOutputStream());
    }

    public static ByteArrayDataOutput newDataOutput(int size) {
        Preconditions.checkArgument(size >= 0, "Invalid size: %s", size);
        return ByteStreams.newDataOutput(new ByteArrayOutputStream(size));
    }

    public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputSteam) {
        return new ByteArrayDataOutputStream(Preconditions.checkNotNull(byteArrayOutputSteam));
    }

    public static OutputStream nullOutputStream() {
        return NULL_OUTPUT_STREAM;
    }

    public static InputStream limit(InputStream in, long limit) {
        return new LimitedInputStream(in, limit);
    }

    @Deprecated
    public static long length(InputSupplier<? extends InputStream> supplier) throws IOException {
        return ByteStreams.asByteSource(supplier).size();
    }

    @Deprecated
    public static boolean equal(InputSupplier<? extends InputStream> supplier1, InputSupplier<? extends InputStream> supplier2) throws IOException {
        return ByteStreams.asByteSource(supplier1).contentEquals(ByteStreams.asByteSource(supplier2));
    }

    public static void readFully(InputStream in, byte[] b2) throws IOException {
        ByteStreams.readFully(in, b2, 0, b2.length);
    }

    public static void readFully(InputStream in, byte[] b2, int off, int len) throws IOException {
        int read = ByteStreams.read(in, b2, off, len);
        if (read != len) {
            throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
        }
    }

    public static void skipFully(InputStream in, long n2) throws IOException {
        long toSkip = n2;
        while (n2 > 0L) {
            long amt = in.skip(n2);
            if (amt == 0L) {
                if (in.read() == -1) {
                    long skipped = toSkip - n2;
                    throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + toSkip + " bytes expected");
                }
                --n2;
                continue;
            }
            n2 -= amt;
        }
    }

    @Deprecated
    public static <T> T readBytes(InputSupplier<? extends InputStream> supplier, ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            InputStream in = (InputStream)closer.register((Closeable)supplier.getInput());
            T t2 = ByteStreams.readBytes(in, processor);
            return t2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
        int read;
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(processor);
        byte[] buf = new byte[4096];
        while ((read = input.read(buf)) != -1 && processor.processBytes(buf, 0, read)) {
        }
        return processor.getResult();
    }

    @Deprecated
    public static HashCode hash(InputSupplier<? extends InputStream> supplier, HashFunction hashFunction) throws IOException {
        return ByteStreams.asByteSource(supplier).hash(hashFunction);
    }

    public static int read(InputStream in, byte[] b2, int off, int len) throws IOException {
        int total;
        int result;
        Preconditions.checkNotNull(in);
        Preconditions.checkNotNull(b2);
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        for (total = 0; total < len && (result = in.read(b2, off + total, len - total)) != -1; total += result) {
        }
        return total;
    }

    @Deprecated
    public static InputSupplier<InputStream> slice(InputSupplier<? extends InputStream> supplier, long offset, long length) {
        return ByteStreams.asInputSupplier(ByteStreams.asByteSource(supplier).slice(offset, length));
    }

    @Deprecated
    public static InputSupplier<InputStream> join(Iterable<? extends InputSupplier<? extends InputStream>> suppliers) {
        Preconditions.checkNotNull(suppliers);
        Iterable<ByteSource> sources = Iterables.transform(suppliers, new Function<InputSupplier<? extends InputStream>, ByteSource>(){

            @Override
            public ByteSource apply(InputSupplier<? extends InputStream> input) {
                return ByteStreams.asByteSource(input);
            }
        });
        return ByteStreams.asInputSupplier(ByteSource.concat(sources));
    }

    @Deprecated
    public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream> ... suppliers) {
        return ByteStreams.join(Arrays.asList(suppliers));
    }

    @Deprecated
    public static ByteSource asByteSource(final InputSupplier<? extends InputStream> supplier) {
        Preconditions.checkNotNull(supplier);
        return new ByteSource(){

            @Override
            public InputStream openStream() throws IOException {
                return (InputStream)supplier.getInput();
            }

            public String toString() {
                return "ByteStreams.asByteSource(" + supplier + ")";
            }
        };
    }

    @Deprecated
    public static ByteSink asByteSink(final OutputSupplier<? extends OutputStream> supplier) {
        Preconditions.checkNotNull(supplier);
        return new ByteSink(){

            @Override
            public OutputStream openStream() throws IOException {
                return (OutputStream)supplier.getOutput();
            }

            public String toString() {
                return "ByteStreams.asByteSink(" + supplier + ")";
            }
        };
    }

    static <S extends InputStream> InputSupplier<S> asInputSupplier(ByteSource source) {
        return Preconditions.checkNotNull(source);
    }

    static <S extends OutputStream> OutputSupplier<S> asOutputSupplier(ByteSink sink) {
        return Preconditions.checkNotNull(sink);
    }

    private static final class LimitedInputStream
    extends FilterInputStream {
        private long left;
        private long mark = -1L;

        LimitedInputStream(InputStream in, long limit) {
            super(in);
            Preconditions.checkNotNull(in);
            Preconditions.checkArgument(limit >= 0L, "limit must be non-negative");
            this.left = limit;
        }

        @Override
        public int available() throws IOException {
            return (int)Math.min((long)this.in.available(), this.left);
        }

        @Override
        public synchronized void mark(int readLimit) {
            this.in.mark(readLimit);
            this.mark = this.left;
        }

        @Override
        public int read() throws IOException {
            if (this.left == 0L) {
                return -1;
            }
            int result = this.in.read();
            if (result != -1) {
                --this.left;
            }
            return result;
        }

        @Override
        public int read(byte[] b2, int off, int len) throws IOException {
            if (this.left == 0L) {
                return -1;
            }
            int result = this.in.read(b2, off, len = (int)Math.min((long)len, this.left));
            if (result != -1) {
                this.left -= (long)result;
            }
            return result;
        }

        @Override
        public synchronized void reset() throws IOException {
            if (!this.in.markSupported()) {
                throw new IOException("Mark not supported");
            }
            if (this.mark == -1L) {
                throw new IOException("Mark not set");
            }
            this.in.reset();
            this.left = this.mark;
        }

        @Override
        public long skip(long n2) throws IOException {
            n2 = Math.min(n2, this.left);
            long skipped = this.in.skip(n2);
            this.left -= skipped;
            return skipped;
        }
    }

    private static class ByteArrayDataOutputStream
    implements ByteArrayDataOutput {
        final DataOutput output;
        final ByteArrayOutputStream byteArrayOutputSteam;

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam) {
            this.byteArrayOutputSteam = byteArrayOutputSteam;
            this.output = new DataOutputStream(byteArrayOutputSteam);
        }

        @Override
        public void write(int b2) {
            try {
                this.output.write(b2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void write(byte[] b2) {
            try {
                this.output.write(b2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void write(byte[] b2, int off, int len) {
            try {
                this.output.write(b2, off, len);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeBoolean(boolean v2) {
            try {
                this.output.writeBoolean(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeByte(int v2) {
            try {
                this.output.writeByte(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeBytes(String s2) {
            try {
                this.output.writeBytes(s2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeChar(int v2) {
            try {
                this.output.writeChar(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeChars(String s2) {
            try {
                this.output.writeChars(s2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeDouble(double v2) {
            try {
                this.output.writeDouble(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeFloat(float v2) {
            try {
                this.output.writeFloat(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeInt(int v2) {
            try {
                this.output.writeInt(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeLong(long v2) {
            try {
                this.output.writeLong(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeShort(int v2) {
            try {
                this.output.writeShort(v2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public void writeUTF(String s2) {
            try {
                this.output.writeUTF(s2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }

    private static class ByteArrayDataInputStream
    implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
            this.input = new DataInputStream(byteArrayInputStream);
        }

        @Override
        public void readFully(byte[] b2) {
            try {
                this.input.readFully(b2);
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public void readFully(byte[] b2, int off, int len) {
            try {
                this.input.readFully(b2, off, len);
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public int skipBytes(int n2) {
            try {
                return this.input.skipBytes(n2);
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public byte readByte() {
            try {
                return this.input.readByte();
            }
            catch (EOFException e2) {
                throw new IllegalStateException(e2);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }

        @Override
        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public short readShort() {
            try {
                return this.input.readShort();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public char readChar() {
            try {
                return this.input.readChar();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public int readInt() {
            try {
                return this.input.readInt();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public long readLong() {
            try {
                return this.input.readLong();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public float readFloat() {
            try {
                return this.input.readFloat();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public double readDouble() {
            try {
                return this.input.readDouble();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public String readLine() {
            try {
                return this.input.readLine();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }

        @Override
        public String readUTF() {
            try {
                return this.input.readUTF();
            }
            catch (IOException e2) {
                throw new IllegalStateException(e2);
            }
        }
    }

    private static final class FastByteArrayOutputStream
    extends ByteArrayOutputStream {
        private FastByteArrayOutputStream() {
        }

        void writeTo(byte[] b2, int off) {
            System.arraycopy(this.buf, 0, b2, off, this.count);
        }
    }
}

