package org.lwjgl.util.mapped;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sun.misc.Unsafe;

final class MappedObjectUnsafe {
  static final Unsafe INSTANCE = getUnsafeInstance();
  
  private static final long BUFFER_ADDRESS_OFFSET = getObjectFieldOffset(ByteBuffer.class, "address");
  
  private static final long BUFFER_CAPACITY_OFFSET = getObjectFieldOffset(ByteBuffer.class, "capacity");
  
  private static final ByteBuffer global = ByteBuffer.allocateDirect(4096);
  
  static ByteBuffer newBuffer(long address, int capacity) {
    if (address <= 0L || capacity < 0)
      throw new IllegalStateException("you almost crashed the jvm"); 
    ByteBuffer buffer = global.duplicate().order(ByteOrder.nativeOrder());
    INSTANCE.putLong(buffer, BUFFER_ADDRESS_OFFSET, address);
    INSTANCE.putInt(buffer, BUFFER_CAPACITY_OFFSET, capacity);
    buffer.position(0);
    buffer.limit(capacity);
    return buffer;
  }
  
  private static long getObjectFieldOffset(Class<?> type, String fieldName) {
    while (type != null) {
      try {
        return INSTANCE.objectFieldOffset(type.getDeclaredField(fieldName));
      } catch (Throwable t) {
        type = type.getSuperclass();
      } 
    } 
    throw new UnsupportedOperationException();
  }
  
  private static Unsafe getUnsafeInstance() {
    Field[] fields = Unsafe.class.getDeclaredFields();
    for (Field field : fields) {
      if (field.getType().equals(Unsafe.class)) {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
          field.setAccessible(true);
          try {
            return (Unsafe)field.get(null);
          } catch (IllegalAccessException e) {
            break;
          } 
        } 
      } 
    } 
    throw new UnsupportedOperationException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\mapped\MappedObjectUnsafe.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */