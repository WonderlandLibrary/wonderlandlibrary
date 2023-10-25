package org.apache.commons.lang3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtils {
  public static <T extends Serializable> T clone(T object) {
    if (object == null)
      return null; 
    byte[] objectData = serialize((Serializable)object);
    ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
    ClassLoaderAwareObjectInputStream in = null;
    try {
      in = new ClassLoaderAwareObjectInputStream(bais, object.getClass().getClassLoader());
      Serializable serializable = (Serializable)in.readObject();
      return (T)serializable;
    } catch (ClassNotFoundException ex) {
      throw new SerializationException("ClassNotFoundException while reading cloned object data", ex);
    } catch (IOException ex) {
      throw new SerializationException("IOException while reading cloned object data", ex);
    } finally {
      try {
        if (in != null)
          in.close(); 
      } catch (IOException ex) {
        throw new SerializationException("IOException on closing cloned object data InputStream.", ex);
      } 
    } 
  }
  
  public static <T extends Serializable> T roundtrip(T msg) {
    return (T)deserialize(serialize((Serializable)msg));
  }
  
  public static void serialize(Serializable obj, OutputStream outputStream) {
    if (outputStream == null)
      throw new IllegalArgumentException("The OutputStream must not be null"); 
    ObjectOutputStream out = null;
    try {
      out = new ObjectOutputStream(outputStream);
      out.writeObject(obj);
    } catch (IOException ex) {
      throw new SerializationException(ex);
    } finally {
      try {
        if (out != null)
          out.close(); 
      } catch (IOException ex) {}
    } 
  }
  
  public static byte[] serialize(Serializable obj) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
    serialize(obj, baos);
    return baos.toByteArray();
  }
  
  public static <T> T deserialize(InputStream inputStream) {
    if (inputStream == null)
      throw new IllegalArgumentException("The InputStream must not be null"); 
    ObjectInputStream in = null;
    try {
      in = new ObjectInputStream(inputStream);
      T obj = (T)in.readObject();
      return obj;
    } catch (ClassCastException ex) {
      throw new SerializationException(ex);
    } catch (ClassNotFoundException ex) {
      throw new SerializationException(ex);
    } catch (IOException ex) {
      throw new SerializationException(ex);
    } finally {
      try {
        if (in != null)
          in.close(); 
      } catch (IOException ex) {}
    } 
  }
  
  public static <T> T deserialize(byte[] objectData) {
    if (objectData == null)
      throw new IllegalArgumentException("The byte[] must not be null"); 
    return deserialize(new ByteArrayInputStream(objectData));
  }
  
  static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
    private static final Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>();
    
    private final ClassLoader classLoader;
    
    public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
      super(in);
      this.classLoader = classLoader;
      primitiveTypes.put("byte", byte.class);
      primitiveTypes.put("short", short.class);
      primitiveTypes.put("int", int.class);
      primitiveTypes.put("long", long.class);
      primitiveTypes.put("float", float.class);
      primitiveTypes.put("double", double.class);
      primitiveTypes.put("boolean", boolean.class);
      primitiveTypes.put("char", char.class);
      primitiveTypes.put("void", void.class);
    }
    
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
      String name = desc.getName();
      try {
        return Class.forName(name, false, this.classLoader);
      } catch (ClassNotFoundException ex) {
        try {
          return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException cnfe) {
          Class<?> cls = primitiveTypes.get(name);
          if (cls != null)
            return cls; 
          throw cnfe;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\SerializationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */