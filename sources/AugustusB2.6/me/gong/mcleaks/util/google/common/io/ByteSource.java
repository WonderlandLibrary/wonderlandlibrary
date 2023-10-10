// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.io;

import me.gong.mcleaks.util.google.common.base.Ascii;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import me.gong.mcleaks.util.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.Arrays;
import me.gong.mcleaks.util.google.common.hash.Hasher;
import me.gong.mcleaks.util.google.common.hash.PrimitiveSink;
import me.gong.mcleaks.util.google.common.hash.Funnels;
import me.gong.mcleaks.util.google.common.hash.HashCode;
import me.gong.mcleaks.util.google.common.hash.HashFunction;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.io.OutputStream;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import me.gong.mcleaks.util.google.common.base.Optional;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;

@GwtIncompatible
public abstract class ByteSource
{
    protected ByteSource() {
    }
    
    public CharSource asCharSource(final Charset charset) {
        return new AsCharSource(charset);
    }
    
    public abstract InputStream openStream() throws IOException;
    
    public InputStream openBufferedStream() throws IOException {
        final InputStream in = this.openStream();
        return (in instanceof BufferedInputStream) ? in : new BufferedInputStream(in);
    }
    
    public ByteSource slice(final long offset, final long length) {
        return new SlicedByteSource(offset, length);
    }
    
    public boolean isEmpty() throws IOException {
        final Optional<Long> sizeIfKnown = this.sizeIfKnown();
        if (sizeIfKnown.isPresent() && sizeIfKnown.get() == 0L) {
            return true;
        }
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return in.read() == -1;
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    public Optional<Long> sizeIfKnown() {
        return Optional.absent();
    }
    
    public long size() throws IOException {
        final Optional<Long> sizeIfKnown = this.sizeIfKnown();
        if (sizeIfKnown.isPresent()) {
            return sizeIfKnown.get();
        }
        Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return this.countBySkipping(in);
        }
        catch (IOException ex) {}
        finally {
            closer.close();
        }
        closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.exhaust(in);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    private long countBySkipping(final InputStream in) throws IOException {
        long count = 0L;
        long skipped;
        while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
            count += skipped;
        }
        return count;
    }
    
    @CanIgnoreReturnValue
    public long copyTo(final OutputStream output) throws IOException {
        Preconditions.checkNotNull(output);
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.copy(in, output);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @CanIgnoreReturnValue
    public long copyTo(final ByteSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            final OutputStream out = closer.register(sink.openStream());
            return ByteStreams.copy(in, out);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public byte[] read() throws IOException {
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.toByteArray(in);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    @Beta
    @CanIgnoreReturnValue
    public <T> T read(final ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        final Closer closer = Closer.create();
        try {
            final InputStream in = closer.register(this.openStream());
            return ByteStreams.readBytes(in, processor);
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public HashCode hash(final HashFunction hashFunction) throws IOException {
        final Hasher hasher = hashFunction.newHasher();
        this.copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }
    
    public boolean contentEquals(final ByteSource other) throws IOException {
        Preconditions.checkNotNull(other);
        final byte[] buf1 = ByteStreams.createBuffer();
        final byte[] buf2 = ByteStreams.createBuffer();
        final Closer closer = Closer.create();
        try {
            final InputStream in1 = closer.register(this.openStream());
            final InputStream in2 = closer.register(other.openStream());
            while (true) {
                final int read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
                final int read2 = ByteStreams.read(in2, buf2, 0, buf2.length);
                if (read1 != read2 || !Arrays.equals(buf1, buf2)) {
                    return false;
                }
                if (read1 != buf1.length) {
                    return true;
                }
            }
        }
        catch (Throwable e) {
            throw closer.rethrow(e);
        }
        finally {
            closer.close();
        }
    }
    
    public static ByteSource concat(final Iterable<? extends ByteSource> sources) {
        return new ConcatenatedByteSource(sources);
    }
    
    public static ByteSource concat(final Iterator<? extends ByteSource> sources) {
        return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Iterator<?>)sources));
    }
    
    public static ByteSource concat(final ByteSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }
    
    public static ByteSource wrap(final byte[] b) {
        return new ByteArrayByteSource(b);
    }
    
    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }
    
    private final class AsCharSource extends CharSource
    {
        final Charset charset;
        
        AsCharSource(final Charset charset) {
            this.charset = Preconditions.checkNotNull(charset);
        }
        
        @Override
        public ByteSource asByteSource(final Charset charset) {
            if (charset.equals(this.charset)) {
                return ByteSource.this;
            }
            return super.asByteSource(charset);
        }
        
        @Override
        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }
        
        @Override
        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }
    
    private final class SlicedByteSource extends ByteSource
    {
        final long offset;
        final long length;
        
        SlicedByteSource(final long offset, final long length) {
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
        
        private InputStream sliceStream(final InputStream in) throws IOException {
            if (this.offset > 0L) {
                long skipped;
                try {
                    skipped = ByteStreams.skipUpTo(in, this.offset);
                }
                catch (Throwable e) {
                    final Closer closer = Closer.create();
                    closer.register(in);
                    try {
                        throw closer.rethrow(e);
                    }
                    finally {
                        closer.close();
                    }
                }
                if (skipped < this.offset) {
                    in.close();
                    return new ByteArrayInputStream(new byte[0]);
                }
            }
            return ByteStreams.limit(in, this.length);
        }
        
        @Override
        public ByteSource slice(final long offset, final long length) {
            Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
            final long maxLength = this.length - offset;
            return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            return this.length == 0L || super.isEmpty();
        }
        
        @Override
        public Optional<Long> sizeIfKnown() {
            final Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
            if (optionalUnslicedSize.isPresent()) {
                final long unslicedSize = optionalUnslicedSize.get();
                final long off = Math.min(this.offset, unslicedSize);
                return Optional.of(Math.min(this.length, unslicedSize - off));
            }
            return Optional.absent();
        }
        
        @Override
        public String toString() {
            return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }
    
    private static class ByteArrayByteSource extends ByteSource
    {
        final byte[] bytes;
        final int offset;
        final int length;
        
        ByteArrayByteSource(final byte[] bytes) {
            this(bytes, 0, bytes.length);
        }
        
        ByteArrayByteSource(final byte[] bytes, final int offset, final int length) {
            this.bytes = bytes;
            this.offset = offset;
            this.length = length;
        }
        
        @Override
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes, this.offset, this.length);
        }
        
        @Override
        public InputStream openBufferedStream() throws IOException {
            return this.openStream();
        }
        
        @Override
        public boolean isEmpty() {
            return this.length == 0;
        }
        
        @Override
        public long size() {
            return this.length;
        }
        
        @Override
        public Optional<Long> sizeIfKnown() {
            return Optional.of((long)this.length);
        }
        
        @Override
        public byte[] read() {
            return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
        }
        
        @Override
        public long copyTo(final OutputStream output) throws IOException {
            output.write(this.bytes, this.offset, this.length);
            return this.length;
        }
        
        @Override
        public <T> T read(final ByteProcessor<T> processor) throws IOException {
            processor.processBytes(this.bytes, this.offset, this.length);
            return processor.getResult();
        }
        
        @Override
        public HashCode hash(final HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes, this.offset, this.length);
        }
        
        @Override
        public ByteSource slice(long offset, long length) {
            Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
            offset = Math.min(offset, this.length);
            length = Math.min(length, this.length - offset);
            final int newOffset = this.offset + (int)offset;
            return new ByteArrayByteSource(this.bytes, newOffset, (int)length);
        }
        
        @Override
        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
        }
    }
    
    private static final class EmptyByteSource extends ByteArrayByteSource
    {
        static final EmptyByteSource INSTANCE;
        
        EmptyByteSource() {
            super(new byte[0]);
        }
        
        @Override
        public CharSource asCharSource(final Charset charset) {
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
        
        static {
            INSTANCE = new EmptyByteSource();
        }
    }
    
    private static final class ConcatenatedByteSource extends ByteSource
    {
        final Iterable<? extends ByteSource> sources;
        
        ConcatenatedByteSource(final Iterable<? extends ByteSource> sources) {
            this.sources = Preconditions.checkNotNull(sources);
        }
        
        @Override
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }
        
        @Override
        public boolean isEmpty() throws IOException {
            for (final ByteSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public Optional<Long> sizeIfKnown() {
            long result = 0L;
            for (final ByteSource source : this.sources) {
                final Optional<Long> sizeIfKnown = source.sizeIfKnown();
                if (!sizeIfKnown.isPresent()) {
                    return Optional.absent();
                }
                result += sizeIfKnown.get();
            }
            return Optional.of(result);
        }
        
        @Override
        public long size() throws IOException {
            long result = 0L;
            for (final ByteSource source : this.sources) {
                result += source.size();
            }
            return result;
        }
        
        @Override
        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }
}
