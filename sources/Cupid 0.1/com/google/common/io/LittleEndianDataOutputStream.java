package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Longs;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Beta
public class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput {
  public LittleEndianDataOutputStream(OutputStream out) {
    super(new DataOutputStream((OutputStream)Preconditions.checkNotNull(out)));
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    this.out.write(b, off, len);
  }
  
  public void writeBoolean(boolean v) throws IOException {
    ((DataOutputStream)this.out).writeBoolean(v);
  }
  
  public void writeByte(int v) throws IOException {
    ((DataOutputStream)this.out).writeByte(v);
  }
  
  @Deprecated
  public void writeBytes(String s) throws IOException {
    ((DataOutputStream)this.out).writeBytes(s);
  }
  
  public void writeChar(int v) throws IOException {
    writeShort(v);
  }
  
  public void writeChars(String s) throws IOException {
    for (int i = 0; i < s.length(); i++)
      writeChar(s.charAt(i)); 
  }
  
  public void writeDouble(double v) throws IOException {
    writeLong(Double.doubleToLongBits(v));
  }
  
  public void writeFloat(float v) throws IOException {
    writeInt(Float.floatToIntBits(v));
  }
  
  public void writeInt(int v) throws IOException {
    this.out.write(0xFF & v);
    this.out.write(0xFF & v >> 8);
    this.out.write(0xFF & v >> 16);
    this.out.write(0xFF & v >> 24);
  }
  
  public void writeLong(long v) throws IOException {
    byte[] bytes = Longs.toByteArray(Long.reverseBytes(v));
    write(bytes, 0, bytes.length);
  }
  
  public void writeShort(int v) throws IOException {
    this.out.write(0xFF & v);
    this.out.write(0xFF & v >> 8);
  }
  
  public void writeUTF(String str) throws IOException {
    ((DataOutputStream)this.out).writeUTF(str);
  }
  
  public void close() throws IOException {
    this.out.close();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\io\LittleEndianDataOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */