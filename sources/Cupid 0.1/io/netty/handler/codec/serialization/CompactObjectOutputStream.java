package io.netty.handler.codec.serialization;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

class CompactObjectOutputStream extends ObjectOutputStream {
  static final int TYPE_FAT_DESCRIPTOR = 0;
  
  static final int TYPE_THIN_DESCRIPTOR = 1;
  
  CompactObjectOutputStream(OutputStream out) throws IOException {
    super(out);
  }
  
  protected void writeStreamHeader() throws IOException {
    writeByte(5);
  }
  
  protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
    Class<?> clazz = desc.forClass();
    if (clazz.isPrimitive() || clazz.isArray() || clazz.isInterface() || desc.getSerialVersionUID() == 0L) {
      write(0);
      super.writeClassDescriptor(desc);
    } else {
      write(1);
      writeUTF(desc.getName());
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\CompactObjectOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */