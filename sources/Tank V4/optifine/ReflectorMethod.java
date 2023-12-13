package optifine;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectorMethod {
   private ReflectorClass reflectorClass;
   private String targetMethodName;
   private Class[] targetMethodParameterTypes;
   private boolean checked;
   private Method targetMethod;

   public ReflectorMethod(ReflectorClass var1, String var2) {
      this(var1, var2, (Class[])null, false);
   }

   public ReflectorMethod(ReflectorClass var1, String var2, Class[] var3) {
      this(var1, var2, var3, false);
   }

   public ReflectorMethod(ReflectorClass var1, String var2, Class[] var3, boolean var4) {
      this.reflectorClass = null;
      this.targetMethodName = null;
      this.targetMethodParameterTypes = null;
      this.checked = false;
      this.targetMethod = null;
      this.reflectorClass = var1;
      this.targetMethodName = var2;
      this.targetMethodParameterTypes = var3;
      if (!var4) {
         Method var5 = this.getTargetMethod();
      }

   }

   public Method getTargetMethod() {
      if (this.checked) {
         return this.targetMethod;
      } else {
         this.checked = true;
         Class var1 = this.reflectorClass.getTargetClass();
         if (var1 == null) {
            return null;
         } else {
            try {
               if (this.targetMethodParameterTypes == null) {
                  Method[] var2 = getMethods(var1, this.targetMethodName);
                  if (var2.length <= 0) {
                     Config.log("(Reflector) Method not present: " + var1.getName() + "." + this.targetMethodName);
                     return null;
                  }

                  if (var2.length > 1) {
                     Config.warn("(Reflector) More than one method found: " + var1.getName() + "." + this.targetMethodName);

                     for(int var3 = 0; var3 < var2.length; ++var3) {
                        Method var4 = var2[var3];
                        Config.warn("(Reflector)  - " + var4);
                     }

                     return null;
                  }

                  this.targetMethod = var2[0];
               } else {
                  this.targetMethod = getMethod(var1, this.targetMethodName, this.targetMethodParameterTypes);
               }

               if (this.targetMethod == null) {
                  Config.log("(Reflector) Method not present: " + var1.getName() + "." + this.targetMethodName);
                  return null;
               } else {
                  this.targetMethod.setAccessible(true);
                  return this.targetMethod;
               }
            } catch (Throwable var5) {
               var5.printStackTrace();
               return null;
            }
         }
      }
   }

   public boolean exists() {
      return this.checked ? this.targetMethod != null : this.getTargetMethod() != null;
   }

   public Class getReturnType() {
      Method var1 = this.getTargetMethod();
      return var1 == null ? null : var1.getReturnType();
   }

   public void deactivate() {
      this.checked = true;
      this.targetMethod = null;
   }

   public static Method getMethod(Class var0, String var1, Class[] var2) {
      Method[] var3 = var0.getDeclaredMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Method var5 = var3[var4];
         if (var5.getName().equals(var1)) {
            Class[] var6 = var5.getParameterTypes();
            if (Reflector.matchesTypes(var2, var6)) {
               return var5;
            }
         }
      }

      return null;
   }

   public static Method[] getMethods(Class var0, String var1) {
      ArrayList var2 = new ArrayList();
      Method[] var3 = var0.getDeclaredMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Method var5 = var3[var4];
         if (var5.getName().equals(var1)) {
            var2.add(var5);
         }
      }

      Method[] var6 = (Method[])var2.toArray(new Method[var2.size()]);
      return var6;
   }
}
