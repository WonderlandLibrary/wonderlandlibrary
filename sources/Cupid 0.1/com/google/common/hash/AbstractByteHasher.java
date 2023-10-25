package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

abstract class AbstractByteHasher extends AbstractHasher {
  private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
  
  protected void update(byte[] b) {
    update(b, 0, b.length);
  }
  
  protected void update(byte[] b, int off, int len) {
    for (int i = off; i < off + len; i++)
      update(b[i]); 
  }
  
  public Hasher putByte(byte b) {
    update(b);
    return this;
  }
  
  public Hasher putBytes(byte[] bytes) {
    Preconditions.checkNotNull(bytes);
    update(bytes);
    return this;
  }
  
  public Hasher putBytes(byte[] bytes, int off, int len) {
    Preconditions.checkPositionIndexes(off, off + len, bytes.length);
    update(bytes, off, len);
    return this;
  }
  
  private Hasher update(int bytes) {
    try {
      update(this.scratch.array(), 0, bytes);
    } finally {
      this.scratch.clear();
    } 
    return this;
  }
  
  public Hasher putShort(short s) {
    this.scratch.putShort(s);
    return update(2);
  }
  
  public Hasher putInt(int i) {
    this.scratch.putInt(i);
    return update(4);
  }
  
  public Hasher putLong(long l) {
    this.scratch.putLong(l);
    return update(8);
  }
  
  public Hasher putChar(char c) {
    this.scratch.putChar(c);
    return update(2);
  }
  
  public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
    funnel.funnel(instance, this);
    return this;
  }
  
  protected abstract void update(byte paramByte);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\AbstractByteHasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */