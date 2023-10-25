package io.netty.handler.codec.serialization;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;

class CompactObjectInputStream extends ObjectInputStream {
  private final ClassResolver classResolver;
  
  CompactObjectInputStream(InputStream in, ClassResolver classResolver) throws IOException {
    super(in);
    this.classResolver = classResolver;
  }
  
  protected void readStreamHeader() throws IOException {
    int version = readByte() & 0xFF;
    if (version != 5)
      throw new StreamCorruptedException("Unsupported version: " + version); 
  }
  
  protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
    String className;
    Class<?> clazz;
    int type = read();
    if (type < 0)
      throw new EOFException(); 
    switch (type) {
      case 0:
        return super.readClassDescriptor();
      case 1:
        className = readUTF();
        clazz = this.classResolver.resolve(className);
        return ObjectStreamClass.lookupAny(clazz);
    } 
    throw new StreamCorruptedException("Unexpected class descriptor type: " + type);
  }
  
  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    Class<?> clazz;
    try {
      clazz = this.classResolver.resolve(desc.getName());
    } catch (ClassNotFoundException ignored) {
      clazz = super.resolveClass(desc);
    } 
    return clazz;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\CompactObjectInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */