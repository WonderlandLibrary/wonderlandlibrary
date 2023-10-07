// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.io;

import java.io.FilterInputStream;
import java.io.DataOutputStream;
import java.io.DataOutput;
import java.io.DataInputStream;
import java.io.DataInput;
import java.io.EOFException;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.io.IOException;
import com.google.common.base.Preconditions;
import java.io.InputStream;
import java.io.OutputStream;
import com.google.common.annotations.Beta;

@Beta
public final class ByteStreams
{
    private static final int BUF_SIZE = 4096;
    private static final OutputStream NULL_OUTPUT_STREAM;
    
    private ByteStreams() {
    }
    
    public static long copy(final InputStream from, final OutputStream to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        final byte[] buf = new byte[4096];
        long total = 0L;
        while (true) {
            final int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }
    
    public static long copy(final ReadableByteChannel from, final WritableByteChannel to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        final ByteBuffer buf = ByteBuffer.allocate(4096);
        long total = 0L;
        while (from.read(buf) != -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                total += to.write(buf);
            }
            buf.clear();
        }
        return total;
    }
    
    public static byte[] toByteArray(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }
    
    static byte[] toByteArray(final InputStream in, final int expectedSize) throws IOException {
        final byte[] bytes = new byte[expectedSize];
        int read;
        for (int remaining = expectedSize; remaining > 0; remaining -= read) {
            final int off = expectedSize - remaining;
            read = in.read(bytes, off, remaining);
            if (read == -1) {
                return Arrays.copyOf(bytes, off);
            }
        }
        final int b = in.read();
        if (b == -1) {
            return bytes;
        }
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        out.write(b);
        copy(in, out);
        final byte[] result = new byte[bytes.length + out.size()];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        out.writeTo(result, bytes.length);
        return result;
    }
    
    public static ByteArrayDataInput newDataInput(final byte[] bytes) {
        return newDataInput(new ByteArrayInputStream(bytes));
    }
    
    public static ByteArrayDataInput newDataInput(final byte[] bytes, final int start) {
        Preconditions.checkPositionIndex(start, bytes.length);
        return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
    }
    
    public static ByteArrayDataInput newDataInput(final ByteArrayInputStream byteArrayInputStream) {
        return new ByteArrayDataInputStream(Preconditions.checkNotNull(byteArrayInputStream));
    }
    
    public static ByteArrayDataOutput newDataOutput() {
        return newDataOutput(new ByteArrayOutputStream());
    }
    
    public static ByteArrayDataOutput newDataOutput(final int size) {
        Preconditions.checkArgument(size >= 0, "Invalid size: %s", size);
        return newDataOutput(new ByteArrayOutputStream(size));
    }
    
    public static ByteArrayDataOutput newDataOutput(final ByteArrayOutputStream byteArrayOutputSteam) {
        return new ByteArrayDataOutputStream(Preconditions.checkNotNull(byteArrayOutputSteam));
    }
    
    public static OutputStream nullOutputStream() {
        return ByteStreams.NULL_OUTPUT_STREAM;
    }
    
    public static InputStream limit(final InputStream in, final long limit) {
        return new LimitedInputStream(in, limit);
    }
    
    public static void readFully(final InputStream in, final byte[] b) throws IOException {
        readFully(in, b, 0, b.length);
    }
    
    public static void readFully(final InputStream in, final byte[] b, final int off, final int len) throws IOException {
        final int read = read(in, b, off, len);
        if (read != len) {
            throw new EOFException(new StringBuilder(81).append("reached end of stream after reading ").append(read).append(" bytes; ").append(len).append(" bytes expected").toString());
        }
    }
    
    public static void skipFully(final InputStream in, long n) throws IOException {
        final long toSkip = n;
        while (n > 0L) {
            final long amt = in.skip(n);
            if (amt == 0L) {
                if (in.read() == -1) {
                    final long skipped = toSkip - n;
                    throw new EOFException(new StringBuilder(100).append("reached end of stream after skipping ").append(skipped).append(" bytes; ").append(toSkip).append(" bytes expected").toString());
                }
                --n;
            }
            else {
                n -= amt;
            }
        }
    }
    
    public static <T> T readBytes(final InputStream input, final ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(processor);
        final byte[] buf = new byte[4096];
        int read;
        do {
            read = input.read(buf);
        } while (read != -1 && processor.processBytes(buf, 0, read));
        return processor.getResult();
    }
    
    public static int read(final InputStream in, final byte[] b, final int off, final int len) throws IOException {
        Preconditions.checkNotNull(in);
        Preconditions.checkNotNull(b);
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int total;
        int result;
        for (total = 0; total < len; total += result) {
            result = in.read(b, off + total, len - total);
            if (result == -1) {
                break;
            }
        }
        return total;
    }
    
    static {
        NULL_OUTPUT_STREAM = new OutputStream() {
            @Override
            public void write(final int b) {
            }
            
            @Override
            public void write(final byte[] b) {
                Preconditions.checkNotNull(b);
            }
            
            @Override
            public void write(final byte[] b, final int off, final int len) {
                Preconditions.checkNotNull(b);
            }
            
            @Override
            public String toString() {
                return "ByteStreams.nullOutputStream()";
            }
        };
    }
    
    private static final class FastByteArrayOutputStream extends ByteArrayOutputStream
    {
        void writeTo(final byte[] b, final int off) {
            System.arraycopy(this.buf, 0, b, off, this.count);
        }
    }
    
    private static class ByteArrayDataInputStream implements ByteArrayDataInput
    {
        final DataInput input;
        
        ByteArrayDataInputStream(final ByteArrayInputStream byteArrayInputStream) {
            this.input = new DataInputStream(byteArrayInputStream);
        }
        
        @Override
        public void readFully(final byte[] b) {
            try {
                this.input.readFully(b);
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public void readFully(final byte[] b, final int off, final int len) {
            try {
                this.input.readFully(b, off, len);
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public int skipBytes(final int n) {
            try {
                return this.input.skipBytes(n);
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public byte readByte() {
            try {
                return this.input.readByte();
            }
            catch (final EOFException e) {
                throw new IllegalStateException(e);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public short readShort() {
            try {
                return this.input.readShort();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public char readChar() {
            try {
                return this.input.readChar();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public int readInt() {
            try {
                return this.input.readInt();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public long readLong() {
            try {
                return this.input.readLong();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public float readFloat() {
            try {
                return this.input.readFloat();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public double readDouble() {
            try {
                return this.input.readDouble();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public String readLine() {
            try {
                return this.input.readLine();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public String readUTF() {
            try {
                return this.input.readUTF();
            }
            catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
    
    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput
    {
        final DataOutput output;
        final ByteArrayOutputStream byteArrayOutputSteam;
        
        ByteArrayDataOutputStream(final ByteArrayOutputStream byteArrayOutputSteam) {
            this.byteArrayOutputSteam = byteArrayOutputSteam;
            this.output = new DataOutputStream(byteArrayOutputSteam);
        }
        
        @Override
        public void write(final int b) {
            try {
                this.output.write(b);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void write(final byte[] b) {
            try {
                this.output.write(b);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) {
            try {
                this.output.write(b, off, len);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeBoolean(final boolean v) {
            try {
                this.output.writeBoolean(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeByte(final int v) {
            try {
                this.output.writeByte(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeBytes(final String s) {
            try {
                this.output.writeBytes(s);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeChar(final int v) {
            try {
                this.output.writeChar(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeChars(final String s) {
            try {
                this.output.writeChars(s);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeDouble(final double v) {
            try {
                this.output.writeDouble(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeFloat(final float v) {
            try {
                this.output.writeFloat(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeInt(final int v) {
            try {
                this.output.writeInt(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeLong(final long v) {
            try {
                this.output.writeLong(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeShort(final int v) {
            try {
                this.output.writeShort(v);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeUTF(final String s) {
            try {
                this.output.writeUTF(s);
            }
            catch (final IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }
    
    private static final class LimitedInputStream extends FilterInputStream
    {
        private long left;
        private long mark;
        
        LimitedInputStream(final InputStream in, final long limit) {
            super(in);
            this.mark = -1L;
            Preconditions.checkNotNull(in);
            Preconditions.checkArgument(limit >= 0L, (Object)"limit must be non-negative");
            this.left = limit;
        }
        
        @Override
        public int available() throws IOException {
            return (int)Math.min(this.in.available(), this.left);
        }
        
        @Override
        public synchronized void mark(final int readLimit) {
            this.in.mark(readLimit);
            this.mark = this.left;
        }
        
        @Override
        public int read() throws IOException {
            if (this.left == 0L) {
                return -1;
            }
            final int result = this.in.read();
            if (result != -1) {
                --this.left;
            }
            return result;
        }
        
        @Override
        public int read(final byte[] b, final int off, int len) throws IOException {
            if (this.left == 0L) {
                return -1;
            }
            len = (int)Math.min(len, this.left);
            final int result = this.in.read(b, off, len);
            if (result != -1) {
                this.left -= result;
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
        public long skip(long n) throws IOException {
            n = Math.min(n, this.left);
            final long skipped = this.in.skip(n);
            this.left -= skipped;
            return skipped;
        }
    }
}
