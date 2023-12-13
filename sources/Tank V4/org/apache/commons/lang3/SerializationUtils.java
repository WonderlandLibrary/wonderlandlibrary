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
   public static Serializable clone(Serializable var0) {
      if (var0 == null) {
         return null;
      } else {
         byte[] var1 = serialize(var0);
         ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
         SerializationUtils.ClassLoaderAwareObjectInputStream var3 = null;

         Serializable var5;
         try {
            var3 = new SerializationUtils.ClassLoaderAwareObjectInputStream(var2, var0.getClass().getClassLoader());
            Serializable var4 = (Serializable)var3.readObject();
            var5 = var4;
         } catch (ClassNotFoundException var10) {
            throw new SerializationException("ClassNotFoundException while reading cloned object data", var10);
         } catch (IOException var11) {
            throw new SerializationException("IOException while reading cloned object data", var11);
         }

         try {
            if (var3 != null) {
               var3.close();
            }

            return var5;
         } catch (IOException var9) {
            throw new SerializationException("IOException on closing cloned object data InputStream.", var9);
         }
      }
   }

   public static Serializable roundtrip(Serializable var0) {
      return (Serializable)deserialize(serialize(var0));
   }

   public static void serialize(Serializable var0, OutputStream var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The OutputStream must not be null");
      } else {
         ObjectOutputStream var2 = null;

         try {
            var2 = new ObjectOutputStream(var1);
            var2.writeObject(var0);
         } catch (IOException var7) {
            throw new SerializationException(var7);
         }

         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var6) {
         }

      }
   }

   public static byte[] serialize(Serializable var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream(512);
      serialize(var0, var1);
      return var1.toByteArray();
   }

   public static Object deserialize(InputStream var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The InputStream must not be null");
      } else {
         ObjectInputStream var1 = null;

         Object var3;
         try {
            var1 = new ObjectInputStream(var0);
            Object var2 = var1.readObject();
            var3 = var2;
         } catch (ClassCastException var8) {
            throw new SerializationException(var8);
         } catch (ClassNotFoundException var9) {
            throw new SerializationException(var9);
         } catch (IOException var10) {
            throw new SerializationException(var10);
         }

         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var7) {
         }

         return var3;
      }
   }

   public static Object deserialize(byte[] var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The byte[] must not be null");
      } else {
         return deserialize((InputStream)(new ByteArrayInputStream(var0)));
      }
   }

   static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
      private static final Map primitiveTypes = new HashMap();
      private final ClassLoader classLoader;

      public ClassLoaderAwareObjectInputStream(InputStream var1, ClassLoader var2) throws IOException {
         super(var1);
         this.classLoader = var2;
         primitiveTypes.put("byte", Byte.TYPE);
         primitiveTypes.put("short", Short.TYPE);
         primitiveTypes.put("int", Integer.TYPE);
         primitiveTypes.put("long", Long.TYPE);
         primitiveTypes.put("float", Float.TYPE);
         primitiveTypes.put("double", Double.TYPE);
         primitiveTypes.put("boolean", Boolean.TYPE);
         primitiveTypes.put("char", Character.TYPE);
         primitiveTypes.put("void", Void.TYPE);
      }

      protected Class resolveClass(ObjectStreamClass var1) throws IOException, ClassNotFoundException {
         String var2 = var1.getName();

         try {
            return Class.forName(var2, false, this.classLoader);
         } catch (ClassNotFoundException var7) {
            try {
               return Class.forName(var2, false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException var6) {
               Class var5 = (Class)primitiveTypes.get(var2);
               if (var5 != null) {
                  return var5;
               } else {
                  throw var6;
               }
            }
         }
      }
   }
}
