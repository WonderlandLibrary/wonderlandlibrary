package javassist.tools.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassMetaobject implements Serializable {
   static final String methodPrefix = "_m_";
   static final int methodPrefixLen = 3;
   private Class javaClass;
   private Constructor[] constructors;
   private Method[] methods;
   public static boolean useContextClassLoader = false;

   public ClassMetaobject(String[] var1) {
      try {
         this.javaClass = this.getClassObject(var1[0]);
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException("not found: " + var1[0] + ", useContextClassLoader: " + Boolean.toString(useContextClassLoader), var3);
      }

      this.constructors = this.javaClass.getConstructors();
      this.methods = null;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF(this.javaClass.getName());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.javaClass = this.getClassObject(var1.readUTF());
      this.constructors = this.javaClass.getConstructors();
      this.methods = null;
   }

   private Class getClassObject(String var1) throws ClassNotFoundException {
      return useContextClassLoader ? Thread.currentThread().getContextClassLoader().loadClass(var1) : Class.forName(var1);
   }

   public final Class getJavaClass() {
      return this.javaClass;
   }

   public final String getName() {
      return this.javaClass.getName();
   }

   public final boolean isInstance(Object var1) {
      return this.javaClass.isInstance(var1);
   }

   public final Object newInstance(Object[] var1) throws CannotCreateException {
      int var2 = this.constructors.length;
      int var3 = 0;

      while(var3 < var2) {
         try {
            return this.constructors[var3].newInstance(var1);
         } catch (IllegalArgumentException var5) {
            ++var3;
         } catch (InstantiationException var6) {
            throw new CannotCreateException(var6);
         } catch (IllegalAccessException var7) {
            throw new CannotCreateException(var7);
         } catch (InvocationTargetException var8) {
            throw new CannotCreateException(var8);
         }
      }

      throw new CannotCreateException("no constructor matches");
   }

   public Object trapFieldRead(String var1) {
      Class var2 = this.getJavaClass();

      try {
         return var2.getField(var1).get((Object)null);
      } catch (NoSuchFieldException var4) {
         throw new RuntimeException(var4.toString());
      } catch (IllegalAccessException var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public void trapFieldWrite(String var1, Object var2) {
      Class var3 = this.getJavaClass();

      try {
         var3.getField(var1).set((Object)null, var2);
      } catch (NoSuchFieldException var5) {
         throw new RuntimeException(var5.toString());
      } catch (IllegalAccessException var6) {
         throw new RuntimeException(var6.toString());
      }
   }

   public static Object invoke(Object var0, int var1, Object[] var2) throws Throwable {
      Method[] var3 = var0.getClass().getMethods();
      int var4 = var3.length;
      String var5 = "_m_" + var1;

      for(int var6 = 0; var6 < var4; ++var6) {
         if (var3[var6].getName().startsWith(var5)) {
            try {
               return var3[var6].invoke(var0, var2);
            } catch (InvocationTargetException var8) {
               throw var8.getTargetException();
            } catch (IllegalAccessException var9) {
               throw new CannotInvokeException(var9);
            }
         }
      }

      throw new CannotInvokeException("cannot find a method");
   }

   public Object trapMethodcall(int var1, Object[] var2) throws Throwable {
      try {
         Method[] var3 = this.getReflectiveMethods();
         return var3[var1].invoke((Object)null, var2);
      } catch (InvocationTargetException var4) {
         throw var4.getTargetException();
      } catch (IllegalAccessException var5) {
         throw new CannotInvokeException(var5);
      }
   }

   public final Method[] getReflectiveMethods() {
      if (this.methods != null) {
         return this.methods;
      } else {
         Class var1 = this.getJavaClass();
         Method[] var2 = var1.getDeclaredMethods();
         int var3 = var2.length;
         int[] var4 = new int[var3];
         int var5 = 0;

         int var6;
         for(var6 = 0; var6 < var3; ++var6) {
            Method var7 = var2[var6];
            String var8 = var7.getName();
            if (var8.startsWith("_m_")) {
               int var9 = 0;
               int var10 = 3;

               while(true) {
                  char var11 = var8.charAt(var10);
                  if ('0' > var11 || var11 > '9') {
                     ++var9;
                     var4[var6] = var9;
                     if (var9 > var5) {
                        var5 = var9;
                     }
                     break;
                  }

                  var9 = var9 * 10 + var11 - 48;
                  ++var10;
               }
            }
         }

         this.methods = new Method[var5];

         for(var6 = 0; var6 < var3; ++var6) {
            if (var4[var6] > 0) {
               this.methods[var4[var6] - 1] = var2[var6];
            }
         }

         return this.methods;
      }
   }

   public final Method getMethod(int var1) {
      return this.getReflectiveMethods()[var1];
   }

   public final String getMethodName(int var1) {
      String var2 = this.getReflectiveMethods()[var1].getName();
      int var3 = 3;

      char var4;
      do {
         var4 = var2.charAt(var3++);
      } while(var4 >= '0' && '9' >= var4);

      return var2.substring(var3);
   }

   public final Class[] getParameterTypes(int var1) {
      return this.getReflectiveMethods()[var1].getParameterTypes();
   }

   public final Class getReturnType(int var1) {
      return this.getReflectiveMethods()[var1].getReturnType();
   }

   public final int getMethodIndex(String var1, Class[] var2) throws NoSuchMethodException {
      Method[] var3 = this.getReflectiveMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4] != null && this.getMethodName(var4).equals(var1) && Arrays.equals(var2, var3[var4].getParameterTypes())) {
            return var4;
         }
      }

      throw new NoSuchMethodException("Method " + var1 + " not found");
   }
}
