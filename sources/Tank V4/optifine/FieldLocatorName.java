package optifine;

import java.lang.reflect.Field;

public class FieldLocatorName implements IFieldLocator {
   private ReflectorClass reflectorClass = null;
   private String targetFieldName = null;

   public FieldLocatorName(ReflectorClass var1, String var2) {
      this.reflectorClass = var1;
      this.targetFieldName = var2;
   }

   public Field getField() {
      Class var1 = this.reflectorClass.getTargetClass();
      if (var1 == null) {
         return null;
      } else {
         try {
            Field var2 = var1.getDeclaredField(this.targetFieldName);
            var2.setAccessible(true);
            return var2;
         } catch (NoSuchFieldException var3) {
            Config.log("(Reflector) Field not present: " + var1.getName() + "." + this.targetFieldName);
            return null;
         } catch (SecurityException var4) {
            var4.printStackTrace();
            return null;
         } catch (Throwable var5) {
            var5.printStackTrace();
            return null;
         }
      }
   }
}
