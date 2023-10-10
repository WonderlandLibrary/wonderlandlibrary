package javassist;

import java.util.HashMap;
import javassist.bytecode.Descriptor;

public class ClassMap extends HashMap {
   private ClassMap parent;

   public ClassMap() {
      this.parent = null;
   }

   ClassMap(ClassMap var1) {
      this.parent = var1;
   }

   public void put(CtClass var1, CtClass var2) {
      this.put(var1.getName(), var2.getName());
   }

   public void put(String var1, String var2) {
      if (var1 != var2) {
         String var3 = toJvmName(var1);
         String var4 = (String)this.get(var3);
         if (var4 == null || !var4.equals(var3)) {
            super.put(var3, toJvmName(var2));
         }

      }
   }

   public void putIfNone(String var1, String var2) {
      if (var1 != var2) {
         String var3 = toJvmName(var1);
         String var4 = (String)this.get(var3);
         if (var4 == null) {
            super.put(var3, toJvmName(var2));
         }

      }
   }

   protected final void put0(Object var1, Object var2) {
      super.put(var1, var2);
   }

   public Object get(Object var1) {
      Object var2 = super.get(var1);
      return var2 == null && this.parent != null ? this.parent.get(var1) : var2;
   }

   public void fix(CtClass var1) {
      this.fix(var1.getName());
   }

   public void fix(String var1) {
      String var2 = toJvmName(var1);
      super.put(var2, var2);
   }

   public static String toJvmName(String var0) {
      return Descriptor.toJvmName(var0);
   }

   public static String toJavaName(String var0) {
      return Descriptor.toJavaName(var0);
   }
}
