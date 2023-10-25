package me.rhys.client.shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class BufferUtils {
  private BufferUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  public static String loadString(String path) {
    InputStream inputStream = BufferUtils.class.getResourceAsStream(path);
    StringBuilder stringBuilder = new StringBuilder();
    if (inputStream != null)
      try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null)
          stringBuilder.append(line).append("\n"); 
        bufferedReader.close();
      } catch (Exception e) {
        e.printStackTrace();
      }  
    return stringBuilder.toString();
  }
  
  public static ByteBuffer createByteBuffer(byte[] array) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
    byteBuffer.put(array).flip();
    return byteBuffer;
  }
  
  public static FloatBuffer createFloatBuffer(float[] array) {
    FloatBuffer byteBuffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
    byteBuffer.put(array).flip();
    return byteBuffer;
  }
  
  public static IntBuffer createIntBuffer(int[] array) {
    IntBuffer byteBuffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
    byteBuffer.put(array).flip();
    return byteBuffer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\shader\BufferUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */