package optifine;

import java.lang.reflect.Field;

public class FieldLocatorType implements IFieldLocator {
   private ReflectorClass reflectorClass;
   private Class targetFieldType;
   private int targetFieldIndex;

   public FieldLocatorType(ReflectorClass var1, Class var2) {
      this(var1, var2, 0);
   }

   public FieldLocatorType(ReflectorClass var1, Class var2, int var3) {
      this.reflectorClass = null;
      this.targetFieldType = null;
      this.reflectorClass = var1;
      this.targetFieldType = var2;
      this.targetFieldIndex = var3;
   }

   public Field getField() {
      Class var1 = this.reflectorClass.getTargetClass();
      if (var1 == null) {
         return null;
      } else {
         try {
            Field[] var2 = var1.getDeclaredFields();
            int var3 = 0;

            for(int var4 = 0; var4 < var2.length; ++var4) {
               Field var5 = var2[var4];
               if (var5.getType() == this.targetFieldType) {
                  if (var3 == this.targetFieldIndex) {
                     var5.setAccessible(true);
                     return var5;
                  }

                  ++var3;
               }
            }

            Config.log("(Reflector) Field not present: " + var1.getName() + ".(type: " + this.targetFieldType + ", index: " + this.targetFieldIndex + ")");
            return null;
         } catch (SecurityException var6) {
            var6.printStackTrace();
            return null;
         } catch (Throwable var7) {
            var7.printStackTrace();
            return null;
         }
      }
   }
}
