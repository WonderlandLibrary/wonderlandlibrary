package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;

abstract class InfoUtilAbstract<T extends CLObject> implements InfoUtil<T> {
  protected abstract int getInfo(T paramT, int paramInt, ByteBuffer paramByteBuffer, PointerBuffer paramPointerBuffer);
  
  protected int getInfoSizeArraySize(T object, int param_name) {
    throw new UnsupportedOperationException();
  }
  
  protected PointerBuffer getSizesBuffer(T object, int param_name) {
    int size = getInfoSizeArraySize(object, param_name);
    PointerBuffer buffer = APIUtil.getBufferPointer(size);
    buffer.limit(size);
    getInfo(object, param_name, buffer.getBuffer(), null);
    return buffer;
  }
  
  public int getInfoInt(T object, int param_name) {
    object.checkValid();
    ByteBuffer buffer = APIUtil.getBufferByte(4);
    getInfo(object, param_name, buffer, null);
    return buffer.getInt(0);
  }
  
  public long getInfoSize(T object, int param_name) {
    object.checkValid();
    PointerBuffer buffer = APIUtil.getBufferPointer();
    getInfo(object, param_name, buffer.getBuffer(), null);
    return buffer.get(0);
  }
  
  public long[] getInfoSizeArray(T object, int param_name) {
    object.checkValid();
    int size = getInfoSizeArraySize(object, param_name);
    PointerBuffer buffer = APIUtil.getBufferPointer(size);
    getInfo(object, param_name, buffer.getBuffer(), null);
    long[] array = new long[size];
    for (int i = 0; i < size; i++)
      array[i] = buffer.get(i); 
    return array;
  }
  
  public long getInfoLong(T object, int param_name) {
    object.checkValid();
    ByteBuffer buffer = APIUtil.getBufferByte(8);
    getInfo(object, param_name, buffer, null);
    return buffer.getLong(0);
  }
  
  public String getInfoString(T object, int param_name) {
    object.checkValid();
    int bytes = getSizeRet(object, param_name);
    if (bytes <= 1)
      return null; 
    ByteBuffer buffer = APIUtil.getBufferByte(bytes);
    getInfo(object, param_name, buffer, null);
    buffer.limit(bytes - 1);
    return APIUtil.getString(buffer);
  }
  
  protected final int getSizeRet(T object, int param_name) {
    PointerBuffer bytes = APIUtil.getBufferPointer();
    int errcode = getInfo(object, param_name, null, bytes);
    if (errcode != 0)
      throw new IllegalArgumentException("Invalid parameter specified: " + LWJGLUtil.toHexString(param_name)); 
    return (int)bytes.get(0);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\InfoUtilAbstract.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */