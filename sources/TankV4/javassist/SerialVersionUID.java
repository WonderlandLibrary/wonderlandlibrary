package javassist;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

public class SerialVersionUID {
   public static void setSerialVersionUID(CtClass var0) throws CannotCompileException, NotFoundException {
      try {
         var0.getDeclaredField("serialVersionUID");
      } catch (NotFoundException var2) {
         if (isSerializable(var0)) {
            CtField var1 = new CtField(CtClass.longType, "serialVersionUID", var0);
            var1.setModifiers(26);
            var0.addField(var1, calculateDefault(var0) + "L");
         }
      }
   }

   private static boolean isSerializable(CtClass var0) throws NotFoundException {
      ClassPool var1 = var0.getClassPool();
      return var0.subtypeOf(var1.get("java.io.Serializable"));
   }

   public static long calculateDefault(CtClass var0) throws CannotCompileException {
      try {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         DataOutputStream var2 = new DataOutputStream(var1);
         ClassFile var3 = var0.getClassFile();
         String var4 = javaName(var0);
         var2.writeUTF(var4);
         CtMethod[] var5 = var0.getDeclaredMethods();
         int var6 = var0.getModifiers();
         if ((var6 & 512) != 0) {
            if (var5.length > 0) {
               var6 |= 1024;
            } else {
               var6 &= -1025;
            }
         }

         var2.writeInt(var6);
         String[] var7 = var3.getInterfaces();

         int var8;
         for(var8 = 0; var8 < var7.length; ++var8) {
            var7[var8] = javaName(var7[var8]);
         }

         Arrays.sort(var7);

         for(var8 = 0; var8 < var7.length; ++var8) {
            var2.writeUTF(var7[var8]);
         }

         CtField[] var17 = var0.getDeclaredFields();
         Arrays.sort(var17, new Comparator() {
            public int compare(Object var1, Object var2) {
               CtField var3 = (CtField)var1;
               CtField var4 = (CtField)var2;
               return var3.getName().compareTo(var4.getName());
            }
         });

         for(int var9 = 0; var9 < var17.length; ++var9) {
            CtField var10 = var17[var9];
            int var11 = var10.getModifiers();
            if ((var11 & 2) == 0 || (var11 & 136) == 0) {
               var2.writeUTF(var10.getName());
               var2.writeInt(var11);
               var2.writeUTF(var10.getFieldInfo2().getDescriptor());
            }
         }

         if (var3.getStaticInitializer() != null) {
            var2.writeUTF("<clinit>");
            var2.writeInt(8);
            var2.writeUTF("()V");
         }

         CtConstructor[] var18 = var0.getDeclaredConstructors();
         Arrays.sort(var18, new Comparator() {
            public int compare(Object var1, Object var2) {
               CtConstructor var3 = (CtConstructor)var1;
               CtConstructor var4 = (CtConstructor)var2;
               return var3.getMethodInfo2().getDescriptor().compareTo(var4.getMethodInfo2().getDescriptor());
            }
         });

         int var12;
         int var19;
         for(var19 = 0; var19 < var18.length; ++var19) {
            CtConstructor var20 = var18[var19];
            var12 = var20.getModifiers();
            if ((var12 & 2) == 0) {
               var2.writeUTF("<init>");
               var2.writeInt(var12);
               var2.writeUTF(var20.getMethodInfo2().getDescriptor().replace('/', '.'));
            }
         }

         Arrays.sort(var5, new Comparator() {
            public int compare(Object var1, Object var2) {
               CtMethod var3 = (CtMethod)var1;
               CtMethod var4 = (CtMethod)var2;
               int var5 = var3.getName().compareTo(var4.getName());
               if (var5 == 0) {
                  var5 = var3.getMethodInfo2().getDescriptor().compareTo(var4.getMethodInfo2().getDescriptor());
               }

               return var5;
            }
         });

         for(var19 = 0; var19 < var5.length; ++var19) {
            CtMethod var22 = var5[var19];
            var12 = var22.getModifiers() & 3391;
            if ((var12 & 2) == 0) {
               var2.writeUTF(var22.getName());
               var2.writeInt(var12);
               var2.writeUTF(var22.getMethodInfo2().getDescriptor().replace('/', '.'));
            }
         }

         var2.flush();
         MessageDigest var21 = MessageDigest.getInstance("SHA");
         byte[] var23 = var21.digest(var1.toByteArray());
         long var24 = 0L;

         for(int var14 = Math.min(var23.length, 8) - 1; var14 >= 0; --var14) {
            var24 = var24 << 8 | (long)(var23[var14] & 255);
         }

         return var24;
      } catch (IOException var15) {
         throw new CannotCompileException(var15);
      } catch (NoSuchAlgorithmException var16) {
         throw new CannotCompileException(var16);
      }
   }

   private static String javaName(CtClass var0) {
      return Descriptor.toJavaName(Descriptor.toJvmName(var0));
   }

   private static String javaName(String var0) {
      return Descriptor.toJavaName(Descriptor.toJvmName(var0));
   }
}
