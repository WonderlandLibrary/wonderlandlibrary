package javassist.util.proxy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import javassist.CannotCompileException;
import javassist.bytecode.ClassFile;

public class FactoryHelper {
   private static Method defineClass1;
   private static Method defineClass2;
   public static final Class[] primitiveTypes;
   public static final String[] wrapperTypes;
   public static final String[] wrapperDesc;
   public static final String[] unwarpMethods;
   public static final String[] unwrapDesc;
   public static final int[] dataSize;

   public static final int typeIndex(Class var0) {
      Class[] var1 = primitiveTypes;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (var1[var3] == var0) {
            return var3;
         }
      }

      throw new RuntimeException("bad type:" + var0.getName());
   }

   public static Class toClass(ClassFile var0, ClassLoader var1) throws CannotCompileException {
      return toClass(var0, var1, (ProtectionDomain)null);
   }

   public static Class toClass(ClassFile var0, ClassLoader var1, ProtectionDomain var2) throws CannotCompileException {
      try {
         byte[] var3 = toBytecode(var0);
         Method var4;
         Object[] var5;
         if (var2 == null) {
            var4 = defineClass1;
            var5 = new Object[]{var0.getName(), var3, new Integer(0), new Integer(var3.length)};
         } else {
            var4 = defineClass2;
            var5 = new Object[]{var0.getName(), var3, new Integer(0), new Integer(var3.length), var2};
         }

         return toClass2(var4, var1, var5);
      } catch (RuntimeException var6) {
         throw var6;
      } catch (InvocationTargetException var7) {
         throw new CannotCompileException(var7.getTargetException());
      } catch (Exception var8) {
         throw new CannotCompileException(var8);
      }
   }

   private static synchronized Class toClass2(Method var0, ClassLoader var1, Object[] var2) throws Exception {
      SecurityActions.setAccessible(var0, true);
      Class var3 = (Class)var0.invoke(var1, var2);
      SecurityActions.setAccessible(var0, false);
      return var3;
   }

   private static byte[] toBytecode(ClassFile var0) throws IOException {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();
      DataOutputStream var2 = new DataOutputStream(var1);
      var0.write(var2);
      var2.close();
      return var1.toByteArray();
   }

   public static void writeFile(ClassFile var0, String var1) throws CannotCompileException {
      try {
         writeFile0(var0, var1);
      } catch (IOException var3) {
         throw new CannotCompileException(var3);
      }
   }

   private static void writeFile0(ClassFile var0, String var1) throws CannotCompileException, IOException {
      String var2 = var0.getName();
      String var3 = var1 + File.separatorChar + var2.replace('.', File.separatorChar) + ".class";
      int var4 = var3.lastIndexOf(File.separatorChar);
      if (var4 > 0) {
         String var5 = var3.substring(0, var4);
         if (!var5.equals(".")) {
            (new File(var5)).mkdirs();
         }
      }

      DataOutputStream var9 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(var3)));

      try {
         var0.write(var9);
      } catch (IOException var8) {
         throw var8;
      }

      var9.close();
   }

   static {
      try {
         Class var0 = Class.forName("java.lang.ClassLoader");
         defineClass1 = SecurityActions.getDeclaredMethod(var0, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE});
         defineClass2 = SecurityActions.getDeclaredMethod(var0, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
      } catch (Exception var1) {
         throw new RuntimeException("cannot initialize");
      }

      primitiveTypes = new Class[]{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE};
      wrapperTypes = new String[]{"java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void"};
      wrapperDesc = new String[]{"(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V"};
      unwarpMethods = new String[]{"booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue"};
      unwrapDesc = new String[]{"()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D"};
      dataSize = new int[]{1, 1, 1, 1, 1, 2, 1, 2};
   }
}
