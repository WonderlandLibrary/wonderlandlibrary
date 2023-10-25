package io.netty.buffer;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteBufOutputStream extends OutputStream implements DataOutput {
  private final ByteBuf buffer;
  
  private final int startIndex;
  
  private final DataOutputStream utf8out = new DataOutputStream(this);
  
  public ByteBufOutputStream(ByteBuf buffer) {
    if (buffer == null)
      throw new NullPointerException("buffer"); 
    this.buffer = buffer;
    this.startIndex = buffer.writerIndex();
  }
  
  public int writtenBytes() {
    return this.buffer.writerIndex() - this.startIndex;
  }
  
  public void write(byte[] b, int off, int len) throws IOException {
    if (len == 0)
      return; 
    this.buffer.writeBytes(b, off, len);
  }
  
  public void write(byte[] b) throws IOException {
    this.buffer.writeBytes(b);
  }
  
  public void write(int b) throws IOException {
    this.buffer.writeByte((byte)b);
  }
  
  public void writeBoolean(boolean v) throws IOException {
    write(v ? 1 : 0);
  }
  
  public void writeByte(int v) throws IOException {
    write(v);
  }
  
  public void writeBytes(String s) throws IOException {
    int len = s.length();
    for (int i = 0; i < len; i++)
      write((byte)s.charAt(i)); 
  }
  
  public void writeChar(int v) throws IOException {
    writeShort((short)v);
  }
  
  public void writeChars(String s) throws IOException {
    int len = s.length();
    for (int i = 0; i < len; i++)
      writeChar(s.charAt(i)); 
  }
  
  public void writeDouble(double v) throws IOException {
    writeLong(Double.doubleToLongBits(v));
  }
  
  public void writeFloat(float v) throws IOException {
    writeInt(Float.floatToIntBits(v));
  }
  
  public void writeInt(int v) throws IOException {
    this.buffer.writeInt(v);
  }
  
  public void writeLong(long v) throws IOException {
    this.buffer.writeLong(v);
  }
  
  public void writeShort(int v) throws IOException {
    this.buffer.writeShort((short)v);
  }
  
  public void writeUTF(String s) throws IOException {
    this.utf8out.writeUTF(s);
  }
  
  public ByteBuf buffer() {
    return this.buffer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\ByteBufOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */