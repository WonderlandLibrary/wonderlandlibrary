package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public abstract class MemberValue {
   ConstPool cp;
   char tag;

   MemberValue(char var1, ConstPool var2) {
      this.cp = var2;
      this.tag = var1;
   }

   abstract Object getValue(ClassLoader var1, ClassPool var2, Method var3) throws ClassNotFoundException;

   abstract Class getType(ClassLoader var1) throws ClassNotFoundException;

   static Class loadClass(ClassLoader var0, String var1) throws ClassNotFoundException, NoSuchClassError {
      try {
         return Class.forName(convertFromArray(var1), true, var0);
      } catch (LinkageError var3) {
         throw new NoSuchClassError(var1, var3);
      }
   }

   private static String convertFromArray(String var0) {
      int var1 = var0.indexOf("[]");
      if (var1 == -1) {
         return var0;
      } else {
         String var2 = var0.substring(0, var1);

         StringBuffer var3;
         for(var3 = new StringBuffer(Descriptor.of(var2)); var1 != -1; var1 = var0.indexOf("[]", var1 + 1)) {
            var3.insert(0, "[");
         }

         return var3.toString().replace('/', '.');
      }
   }

   public abstract void accept(MemberValueVisitor var1);

   public abstract void write(AnnotationsWriter var1) throws IOException;
}
