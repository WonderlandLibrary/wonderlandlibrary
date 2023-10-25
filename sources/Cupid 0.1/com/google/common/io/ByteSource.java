package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.PrimitiveSink;
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

public abstract class ByteSource implements InputSupplier<InputStream> {
  private static final int BUF_SIZE = 4096;
  
  public CharSource asCharSource(Charset charset) {
    return new AsCharSource(charset);
  }
  
  @Deprecated
  public final InputStream getInput() throws IOException {
    return openStream();
  }
  
  public InputStream openBufferedStream() throws IOException {
    InputStream in = openStream();
    return (in instanceof BufferedInputStream) ? in : new BufferedInputStream(in);
  }
  
  public ByteSource slice(long offset, long length) {
    return new SlicedByteSource(offset, length);
  }
  
  public boolean isEmpty() throws IOException {
    Closer closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      return (in.read() == -1);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public long size() throws IOException {
    Closer closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      return countBySkipping(in);
    } catch (IOException e) {
    
    } finally {
      closer.close();
    } 
    closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      return countByReading(in);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  private long countBySkipping(InputStream in) throws IOException {
    long count = 0L;
    while (true) {
      long skipped = in.skip(Math.min(in.available(), 2147483647));
      if (skipped <= 0L) {
        if (in.read() == -1)
          return count; 
        if (count == 0L && in.available() == 0)
          throw new IOException(); 
        count++;
        continue;
      } 
      count += skipped;
    } 
  }
  
  private static final byte[] countBuffer = new byte[4096];
  
  private long countByReading(InputStream in) throws IOException {
    long count = 0L;
    long read;
    while ((read = in.read(countBuffer)) != -1L)
      count += read; 
    return count;
  }
  
  public long copyTo(OutputStream output) throws IOException {
    Preconditions.checkNotNull(output);
    Closer closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      return ByteStreams.copy(in, output);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public long copyTo(ByteSink sink) throws IOException {
    Preconditions.checkNotNull(sink);
    Closer closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      OutputStream out = closer.<OutputStream>register(sink.openStream());
      return ByteStreams.copy(in, out);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public byte[] read() throws IOException {
    Closer closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      return ByteStreams.toByteArray(in);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  @Beta
  public <T> T read(ByteProcessor<T> processor) throws IOException {
    Preconditions.checkNotNull(processor);
    Closer closer = Closer.create();
    try {
      InputStream in = closer.<InputStream>register(openStream());
      return (T)ByteStreams.readBytes(in, (ByteProcessor)processor);
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public HashCode hash(HashFunction hashFunction) throws IOException {
    Hasher hasher = hashFunction.newHasher();
    copyTo(Funnels.asOutputStream((PrimitiveSink)hasher));
    return hasher.hash();
  }
  
  public boolean contentEquals(ByteSource other) throws IOException {
    Preconditions.checkNotNull(other);
    byte[] buf1 = new byte[4096];
    byte[] buf2 = new byte[4096];
    Closer closer = Closer.create();
    try {
      InputStream in1 = closer.<InputStream>register(openStream());
      InputStream in2 = closer.<InputStream>register(other.openStream());
      while (true) {
        int read1 = ByteStreams.read(in1, buf1, 0, 4096);
        int read2 = ByteStreams.read(in2, buf2, 0, 4096);
        if (read1 != read2 || !Arrays.equals(buf1, buf2))
          return false; 
        if (read1 != 4096)
          return true; 
      } 
    } catch (Throwable e) {
      throw closer.rethrow(e);
    } finally {
      closer.close();
    } 
  }
  
  public static ByteSource concat(Iterable<? extends ByteSource> sources) {
    return new ConcatenatedByteSource(sources);
  }
  
  public static ByteSource concat(Iterator<? extends ByteSource> sources) {
    return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf(sources));
  }
  
  public static ByteSource concat(ByteSource... sources) {
    return concat((Iterable<? extends ByteSource>)ImmutableList.copyOf((Object[])sources));
  }
  
  public static ByteSource wrap(byte[] b) {
    return new ByteArrayByteSource(b);
  }
  
  public static ByteSource empty() {
    return EmptyByteSource.INSTANCE;
  }
  
  public abstract InputStream openStream() throws IOException;
  
  private final class AsCharSource extends CharSource {
    private final Charset charset;
    
    private AsCharSource(Charset charset) {
      this.charset = (Charset)Preconditions.checkNotNull(charset);
    }
    
    public Reader openStream() throws IOException {
      return new InputStreamReader(ByteSource.this.openStream(), this.charset);
    }
    
    public String toString() {
      return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
    }
  }
  
  private final class SlicedByteSource extends ByteSource {
    private final long offset;
    
    private final long length;
    
    private SlicedByteSource(long offset, long length) {
      Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", new Object[] { Long.valueOf(offset) });
      Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", new Object[] { Long.valueOf(length) });
      this.offset = offset;
      this.length = length;
    }
    
    public InputStream openStream() throws IOException {
      return sliceStream(ByteSource.this.openStream());
    }
    
    public InputStream openBufferedStream() throws IOException {
      return sliceStream(ByteSource.this.openBufferedStream());
    }
    
    private InputStream sliceStream(InputStream in) throws IOException {
      if (this.offset > 0L)
        try {
          ByteStreams.skipFully(in, this.offset);
        } catch (Throwable e) {
          Closer closer = Closer.create();
          closer.register(in);
          try {
            throw closer.rethrow(e);
          } finally {
            closer.close();
          } 
        }  
      return ByteStreams.limit(in, this.length);
    }
    
    public ByteSource slice(long offset, long length) {
      Preconditions.checkArgument((offset >= 0L), "offset (%s) may not be negative", new Object[] { Long.valueOf(offset) });
      Preconditions.checkArgument((length >= 0L), "length (%s) may not be negative", new Object[] { Long.valueOf(length) });
      long maxLength = this.length - offset;
      return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
    }
    
    public boolean isEmpty() throws IOException {
      return (this.length == 0L || super.isEmpty());
    }
    
    public String toString() {
      return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
    }
  }
  
  private static class ByteArrayByteSource extends ByteSource {
    protected final byte[] bytes;
    
    protected ByteArrayByteSource(byte[] bytes) {
      this.bytes = (byte[])Preconditions.checkNotNull(bytes);
    }
    
    public InputStream openStream() {
      return new ByteArrayInputStream(this.bytes);
    }
    
    public InputStream openBufferedStream() throws IOException {
      return openStream();
    }
    
    public boolean isEmpty() {
      return (this.bytes.length == 0);
    }
    
    public long size() {
      return this.bytes.length;
    }
    
    public byte[] read() {
      return (byte[])this.bytes.clone();
    }
    
    public long copyTo(OutputStream output) throws IOException {
      output.write(this.bytes);
      return this.bytes.length;
    }
    
    public <T> T read(ByteProcessor<T> processor) throws IOException {
      processor.processBytes(this.bytes, 0, this.bytes.length);
      return processor.getResult();
    }
    
    public HashCode hash(HashFunction hashFunction) throws IOException {
      return hashFunction.hashBytes(this.bytes);
    }
    
    public String toString() {
      return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes), 30, "...") + ")";
    }
  }
  
  private static final class EmptyByteSource extends ByteArrayByteSource {
    private static final EmptyByteSource INSTANCE = new EmptyByteSource();
    
    private EmptyByteSource() {
      super(new byte[0]);
    }
    
    public CharSource asCharSource(Charset charset) {
      Preconditions.checkNotNull(charset);
      return CharSource.empty();
    }
    
    public byte[] read() {
      return this.bytes;
    }
    
    public String toString() {
      return "ByteSource.empty()";
    }
  }
  
  private static final class ConcatenatedByteSource extends ByteSource {
    private final Iterable<? extends ByteSource> sources;
    
    ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
      this.sources = (Iterable<? extends ByteSource>)Preconditions.checkNotNull(sources);
    }
    
    public InputStream openStream() throws IOException {
      return new MultiInputStream(this.sources.iterator());
    }
    
    public boolean isEmpty() throws IOException {
      for (ByteSource source : this.sources) {
        if (!source.isEmpty())
          return false; 
      } 
      return true;
    }
    
    public long size() throws IOException {
      long result = 0L;
      for (ByteSource source : this.sources)
        result += source.size(); 
      return result;
    }
    
    public String toString() {
      return "ByteSource.concat(" + this.sources + ")";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\ByteSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */