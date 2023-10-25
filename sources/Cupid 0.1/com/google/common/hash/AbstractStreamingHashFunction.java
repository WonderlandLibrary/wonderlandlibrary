package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

abstract class AbstractStreamingHashFunction implements HashFunction {
  public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
    return newHasher().<T>putObject(instance, funnel).hash();
  }
  
  public HashCode hashUnencodedChars(CharSequence input) {
    return newHasher().putUnencodedChars(input).hash();
  }
  
  public HashCode hashString(CharSequence input, Charset charset) {
    return newHasher().putString(input, charset).hash();
  }
  
  public HashCode hashInt(int input) {
    return newHasher().putInt(input).hash();
  }
  
  public HashCode hashLong(long input) {
    return newHasher().putLong(input).hash();
  }
  
  public HashCode hashBytes(byte[] input) {
    return newHasher().putBytes(input).hash();
  }
  
  public HashCode hashBytes(byte[] input, int off, int len) {
    return newHasher().putBytes(input, off, len).hash();
  }
  
  public Hasher newHasher(int expectedInputSize) {
    Preconditions.checkArgument((expectedInputSize >= 0));
    return newHasher();
  }
  
  protected static abstract class AbstractStreamingHasher extends AbstractHasher {
    private final ByteBuffer buffer;
    
    private final int bufferSize;
    
    private final int chunkSize;
    
    protected AbstractStreamingHasher(int chunkSize) {
      this(chunkSize, chunkSize);
    }
    
    protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
      Preconditions.checkArgument((bufferSize % chunkSize == 0));
      this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
      this.bufferSize = bufferSize;
      this.chunkSize = chunkSize;
    }
    
    protected void processRemaining(ByteBuffer bb) {
      bb.position(bb.limit());
      bb.limit(this.chunkSize + 7);
      while (bb.position() < this.chunkSize)
        bb.putLong(0L); 
      bb.limit(this.chunkSize);
      bb.flip();
      process(bb);
    }
    
    public final Hasher putBytes(byte[] bytes) {
      return putBytes(bytes, 0, bytes.length);
    }
    
    public final Hasher putBytes(byte[] bytes, int off, int len) {
      return putBytes(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
    }
    
    private Hasher putBytes(ByteBuffer readBuffer) {
      if (readBuffer.remaining() <= this.buffer.remaining()) {
        this.buffer.put(readBuffer);
        munchIfFull();
        return this;
      } 
      int bytesToCopy = this.bufferSize - this.buffer.position();
      for (int i = 0; i < bytesToCopy; i++)
        this.buffer.put(readBuffer.get()); 
      munch();
      while (readBuffer.remaining() >= this.chunkSize)
        process(readBuffer); 
      this.buffer.put(readBuffer);
      return this;
    }
    
    public final Hasher putUnencodedChars(CharSequence charSequence) {
      for (int i = 0; i < charSequence.length(); i++)
        putChar(charSequence.charAt(i)); 
      return this;
    }
    
    public final Hasher putByte(byte b) {
      this.buffer.put(b);
      munchIfFull();
      return this;
    }
    
    public final Hasher putShort(short s) {
      this.buffer.putShort(s);
      munchIfFull();
      return this;
    }
    
    public final Hasher putChar(char c) {
      this.buffer.putChar(c);
      munchIfFull();
      return this;
    }
    
    public final Hasher putInt(int i) {
      this.buffer.putInt(i);
      munchIfFull();
      return this;
    }
    
    public final Hasher putLong(long l) {
      this.buffer.putLong(l);
      munchIfFull();
      return this;
    }
    
    public final <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
      funnel.funnel(instance, this);
      return this;
    }
    
    public final HashCode hash() {
      munch();
      this.buffer.flip();
      if (this.buffer.remaining() > 0)
        processRemaining(this.buffer); 
      return makeHash();
    }
    
    private void munchIfFull() {
      if (this.buffer.remaining() < 8)
        munch(); 
    }
    
    private void munch() {
      this.buffer.flip();
      while (this.buffer.remaining() >= this.chunkSize)
        process(this.buffer); 
      this.buffer.compact();
    }
    
    protected abstract void process(ByteBuffer param1ByteBuffer);
    
    abstract HashCode makeHash();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\AbstractStreamingHashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */