package org.lwjgl.util;

import java.nio.ByteBuffer;

public interface ReadableColor {
  public static final ReadableColor RED = new Color(255, 0, 0);
  
  public static final ReadableColor ORANGE = new Color(255, 128, 0);
  
  public static final ReadableColor YELLOW = new Color(255, 255, 0);
  
  public static final ReadableColor GREEN = new Color(0, 255, 0);
  
  public static final ReadableColor CYAN = new Color(0, 255, 255);
  
  public static final ReadableColor BLUE = new Color(0, 0, 255);
  
  public static final ReadableColor PURPLE = new Color(255, 0, 255);
  
  public static final ReadableColor WHITE = new Color(255, 255, 255);
  
  public static final ReadableColor BLACK = new Color(0, 0, 0);
  
  public static final ReadableColor LTGREY = new Color(192, 192, 192);
  
  public static final ReadableColor DKGREY = new Color(64, 64, 64);
  
  public static final ReadableColor GREY = new Color(128, 128, 128);
  
  int getRed();
  
  int getGreen();
  
  int getBlue();
  
  int getAlpha();
  
  byte getRedByte();
  
  byte getGreenByte();
  
  byte getBlueByte();
  
  byte getAlphaByte();
  
  void writeRGBA(ByteBuffer paramByteBuffer);
  
  void writeRGB(ByteBuffer paramByteBuffer);
  
  void writeABGR(ByteBuffer paramByteBuffer);
  
  void writeBGR(ByteBuffer paramByteBuffer);
  
  void writeBGRA(ByteBuffer paramByteBuffer);
  
  void writeARGB(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\ReadableColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */