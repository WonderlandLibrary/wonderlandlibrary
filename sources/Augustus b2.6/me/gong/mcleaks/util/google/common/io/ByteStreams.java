// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

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
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.io.InputStream;
import java.io.OutputStream;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import me.gong.mcleaks.util.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public final class ByteStreams
{
    private static final int ZERO_COPY_CHUNK_SIZE = 524288;
    private static final OutputStream NULL_OUTPUT_STREAM;
    
    static byte[] createBuffer() {
        return new byte[8192];
    }
    
    private ByteStreams() {
    }
    
    @CanIgnoreReturnValue
    public static long copy(final InputStream from, final OutputStream to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        final byte[] buf = createBuffer();
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
    
    @CanIgnoreReturnValue
    public static long copy(final ReadableByteChannel from, final WritableByteChannel to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        if (from instanceof FileChannel) {
            final FileChannel sourceChannel = (FileChannel)from;
            long position;
            final long oldPosition = position = sourceChannel.position();
            long copied;
            do {
                copied = sourceChannel.transferTo(position, 524288L, to);
                position += copied;
                sourceChannel.position(position);
            } while (copied > 0L || position < sourceChannel.size());
            return position - oldPosition;
        }
        final ByteBuffer buf = ByteBuffer.wrap(createBuffer());
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
        final ByteArrayOutputStream out = new ByteArrayOutputStream(Math.max(32, in.available()));
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
    
    @CanIgnoreReturnValue
    public static long exhaust(final InputStream in) throws IOException {
        long total = 0L;
        final byte[] buf = createBuffer();
        long read;
        while ((read = in.read(buf)) != -1L) {
            total += read;
        }
        return total;
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
        if (size < 0) {
            throw new IllegalArgumentException(String.format("Invalid size: %s", size));
        }
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
            throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
        }
    }
    
    public static void skipFully(final InputStream in, final long n) throws IOException {
        final long skipped = skipUpTo(in, n);
        if (skipped < n) {
            throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + n + " bytes expected");
        }
    }
    
    static long skipUpTo(final InputStream in, final long n) throws IOException {
        long totalSkipped = 0L;
        final byte[] buf = createBuffer();
        while (totalSkipped < n) {
            final long remaining = n - totalSkipped;
            long skipped = skipSafely(in, remaining);
            if (skipped == 0L) {
                final int skip = (int)Math.min(remaining, buf.length);
                if ((skipped = in.read(buf, 0, skip)) == -1L) {
                    break;
                }
            }
            totalSkipped += skipped;
        }
        return totalSkipped;
    }
    
    private static long skipSafely(final InputStream in, final long n) throws IOException {
        final int available = in.available();
        return (available == 0) ? 0L : in.skip(Math.min(available, n));
    }
    
    @CanIgnoreReturnValue
    public static <T> T readBytes(final InputStream input, final ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(processor);
        final byte[] buf = createBuffer();
        int read;
        do {
            read = input.read(buf);
        } while (read != -1 && processor.processBytes(buf, 0, read));
        return processor.getResult();
    }
    
    @CanIgnoreReturnValue
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
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public void readFully(final byte[] b, final int off, final int len) {
            try {
                this.input.readFully(b, off, len);
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public int skipBytes(final int n) {
            try {
                return this.input.skipBytes(n);
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public byte readByte() {
            try {
                return this.input.readByte();
            }
            catch (EOFException e) {
                throw new IllegalStateException(e);
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
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public short readShort() {
            try {
                return this.input.readShort();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public char readChar() {
            try {
                return this.input.readChar();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public int readInt() {
            try {
                return this.input.readInt();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public long readLong() {
            try {
                return this.input.readLong();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public float readFloat() {
            try {
                return this.input.readFloat();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public double readDouble() {
            try {
                return this.input.readDouble();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public String readLine() {
            try {
                return this.input.readLine();
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public String readUTF() {
            try {
                return this.input.readUTF();
            }
            catch (IOException e) {
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
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void write(final byte[] b) {
            try {
                this.output.write(b);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) {
            try {
                this.output.write(b, off, len);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeBoolean(final boolean v) {
            try {
                this.output.writeBoolean(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeByte(final int v) {
            try {
                this.output.writeByte(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeBytes(final String s) {
            try {
                this.output.writeBytes(s);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeChar(final int v) {
            try {
                this.output.writeChar(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeChars(final String s) {
            try {
                this.output.writeChars(s);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeDouble(final double v) {
            try {
                this.output.writeDouble(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeFloat(final float v) {
            try {
                this.output.writeFloat(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeInt(final int v) {
            try {
                this.output.writeInt(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeLong(final long v) {
            try {
                this.output.writeLong(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeShort(final int v) {
            try {
                this.output.writeShort(v);
            }
            catch (IOException impossible) {
                throw new AssertionError((Object)impossible);
            }
        }
        
        @Override
        public void writeUTF(final String s) {
            try {
                this.output.writeUTF(s);
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
