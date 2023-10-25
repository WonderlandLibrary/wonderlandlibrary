package org.lwjgl.util;

import java.nio.ByteBuffer;

public interface WritableColor {
  void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void set(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4);
  
  void set(int paramInt1, int paramInt2, int paramInt3);
  
  void set(byte paramByte1, byte paramByte2, byte paramByte3);
  
  void setRed(int paramInt);
  
  void setGreen(int paramInt);
  
  void setBlue(int paramInt);
  
  void setAlpha(int paramInt);
  
  void setRed(byte paramByte);
  
  void setGreen(byte paramByte);
  
  void setBlue(byte paramByte);
  
  void setAlpha(byte paramByte);
  
  void readRGBA(ByteBuffer paramByteBuffer);
  
  void readRGB(ByteBuffer paramByteBuffer);
  
  void readARGB(ByteBuffer paramByteBuffer);
  
  void readBGRA(ByteBuffer paramByteBuffer);
  
  void readBGR(ByteBuffer paramByteBuffer);
  
  void readABGR(ByteBuffer paramByteBuffer);
  
  void setColor(ReadableColor paramReadableColor);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\WritableColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */