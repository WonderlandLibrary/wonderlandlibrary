/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteProcessor;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.Closer;
import com.google.common.io.InputSupplier;
import com.google.common.io.MultiInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

public abstract class ByteSource
implements InputSupplier<InputStream> {
    private static final int BUF_SIZE = 4096;
    private static final byte[] countBuffer = new byte[4096];

    protected ByteSource() {
    }

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public abstract InputStream openStream() throws IOException;

    @Override
    @Deprecated
    public final InputStream getInput() throws IOException {
        return this.openStream();
    }

    public InputStream openBufferedStream() throws IOException {
        InputStream in = this.openStream();
        return in instanceof BufferedInputStream ? (BufferedInputStream)in : new BufferedInputStream(in);
    }

    public ByteSource slice(long offset, long length) {
        return new SlicedByteSource(offset, length);
    }

    public boolean isEmpty() throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
            boolean bl = in.read() == -1;
            return bl;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long size() throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
            long l2 = this.countBySkipping(in);
            return l2;
        }
        catch (IOException e2) {
        }
        finally {
            closer.close();
        }
        closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
            long l3 = this.countByReading(in);
            return l3;
        }
        catch (Throwable e3) {
            throw closer.rethrow(e3);
        }
        finally {
            closer.close();
        }
    }

    private long countBySkipping(InputStream in) throws IOException {
        long count = 0L;
        while (true) {
            long skipped;
            if ((skipped = in.skip(Math.min(in.available(), Integer.MAX_VALUE))) <= 0L) {
                if (in.read() == -1) {
                    return count;
                }
                if (count == 0L && in.available() == 0) {
                    throw new IOException();
                }
                ++count;
                continue;
            }
            count += skipped;
        }
    }

    private long countByReading(InputStream in) throws IOException {
        long read;
        long count = 0L;
        while ((read = (long)in.read(countBuffer)) != -1L) {
            count += read;
        }
        return count;
    }

    public long copyTo(OutputStream output) throws IOException {
        Preconditions.checkNotNull(output);
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
            long l2 = ByteStreams.copy(in, output);
            return l2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public long copyTo(ByteSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
            OutputStream out = closer.register(sink.openStream());
            long l2 = ByteStreams.copy(in, out);
            return l2;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public byte[] read() throws IOException {
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
            byte[] byArray = ByteStreams.toByteArray(in);
            return byArray;
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    @Beta
    public <T> T read(ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            InputStream in = closer.register(this.openStream());
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

    public HashCode hash(HashFunction hashFunction) throws IOException {
        Hasher hasher = hashFunction.newHasher();
        this.copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean contentEquals(ByteSource other) throws IOException {
        Preconditions.checkNotNull(other);
        byte[] buf1 = new byte[4096];
        byte[] buf2 = new byte[4096];
        Closer closer = Closer.create();
        try {
            InputStream in1 = closer.register(this.openStream());
            InputStream in2 = closer.register(other.openStream());
            while (true) {
                int read2;
                int read1;
                if ((read1 = ByteStreams.read(in1, buf1, 0, 4096)) != (read2 = ByteStreams.read(in2, buf2, 0, 4096)) || !Arrays.equals(buf1, buf2)) {
                    boolean bl = false;
                    return bl;
                }
                if (read1 != 4096) {
                    boolean bl = true;
                    return bl;
                }
                continue;
                break;
            }
        }
        catch (Throwable e2) {
            throw closer.rethrow(e2);
        }
        finally {
            closer.close();
        }
    }

    public static ByteSource concat(Iterable<? extends ByteSource> sources) {
        return new ConcatenatedByteSource(sources);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> sources) {
        return ByteSource.concat(ImmutableList.copyOf(sources));
    }

    public static ByteSource concat(ByteSource ... sources) {
        return ByteSource.concat(ImmutableList.copyOf(sources));
    }

    public static ByteSource wrap(byte[] b2) {
        return new ByteArrayByteSource(b2);
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }

    private static final class ConcatenatedByteSource
    extends ByteSource {
        private final Iterable<? extends ByteSource> sources;

        ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
            this.sources = Preconditions.checkNotNull(sources);
        }

        @Override
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        @Override
        public boolean isEmpty() throws IOException {
            for (ByteSource byteSource : this.sources) {
                if (byteSource.isEmpty()) continue;
                return false;
            }
            return true;
        }

        @Override
        public long size() throws IOException {
            long result = 0L;
            for (ByteSource byteSource : this.sources) {
                result += byteSource.size();
            }
            return result;
        }

        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }

    private static final class EmptyByteSource
    extends ByteArrayByteSource {
        private static final EmptyByteSource INSTANCE = new EmptyByteSource();

        private EmptyByteSource() {
            super(new byte[0]);
        }

        @Override
        public CharSource asCharSource(Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }

        @Override
        public byte[] read() {
            return this.bytes;
        }

        @Override
        public String toString() {
            return "ByteSource.empty()";
        }
    }

    private static class ByteArrayByteSource
    extends ByteSource {
        protected final byte[] bytes;

        protected ByteArrayByteSource(byte[] bytes) {
            this.bytes = Preconditions.checkNotNull(bytes);
        }

        @Override
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes);
        }

        @Override
        public InputStream openBufferedStream() throws IOException {
            return this.openStream();
        }

        @Override
        public boolean isEmpty() {
            return this.bytes.length == 0;
        }

        @Override
        public long size() {
            return this.bytes.length;
        }

        @Override
        public byte[] read() {
            return (byte[])this.bytes.clone();
        }

        @Override
        public long copyTo(OutputStream output) throws IOException {
            output.write(this.bytes);
            return this.bytes.length;
        }

        @Override
        public <T> T read(ByteProcessor<T> processor) throws IOException {
            processor.processBytes(this.bytes, 0, this.bytes.length);
            return processor.getResult();
        }

        @Override
        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes);
        }

        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "...") + ")";
        }
    }

    private final class SlicedByteSource
    extends ByteSource {
        private final long offset;
        private final long length;

        private SlicedByteSource(long offset, long length) {
            Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
            this.offset = offset;
            this.length = length;
        }

        @Override
        public InputStream openStream() throws IOException {
            return this.sliceStream(ByteSource.this.openStream());
        }

        @Override
        public InputStream openBufferedStream() throws IOException {
            return this.sliceStream(ByteSource.this.openBufferedStream());
        }

        private InputStream sliceStream(InputStream in) throws IOException {
            if (this.offset > 0L) {
                try {
                    ByteStreams.skipFully(in, this.offset);
                }
                catch (Throwable e2) {
                    Closer closer = Closer.create();
                    closer.register(in);
                    try {
                        throw closer.rethrow(e2);
                    }
                    catch (Throwable throwable) {
                        closer.close();
                        throw throwable;
                    }
                }
            }
            return ByteStreams.limit(in, this.length);
        }

        @Override
        public ByteSource slice(long offset, long length) {
            Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
            long maxLength = this.length - offset;
            return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
        }

        @Override
        public boolean isEmpty() throws IOException {
            return this.length == 0L || super.isEmpty();
        }

        public String toString() {
            return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }

    private final class AsCharSource
    extends CharSource {
        private final Charset charset;

        private AsCharSource(Charset charset) {
            this.charset = Preconditions.checkNotNull(charset);
        }

        @Override
        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }
}

