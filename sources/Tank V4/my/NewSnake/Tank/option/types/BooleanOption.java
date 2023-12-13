package my.NewSnake.Tank.option.types;

import java.lang.reflect.Field;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

public class BooleanOption extends Option {
   private boolean isModType;

   public boolean isModType() {
      return this.isModType;
   }

   public BooleanOption(String var1, String var2, boolean var3, Module var4, boolean var5) {
      super(var1, var2, var3, var4);
      this.isModType = var5;
   }

   public void setValue(Boolean var1) {
      super.setValue(var1);
      Field[] var2;
      int var3 = (var2 = this.getModule().getClass().getDeclaredFields()).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         var5.setAccessible(true);
         if (var5.isAnnotationPresent(Option.Op.class) && var5.getName().equalsIgnoreCase(this.getId())) {
            try {
               if (var5.getType().isAssignableFrom(Boolean.TYPE)) {
                  var5.setBoolean(this.getModule(), var1);
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public void setValueHard(Boolean var1) {
      super.setValue(var1);
      Field[] var2;
      int var3 = (var2 = this.getModule().getClass().getDeclaredFields()).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         var5.setAccessible(true);
         if (var5.isAnnotationPresent(Option.Op.class) && var5.getName().equalsIgnoreCase(this.getId())) {
            try {
               if (var5.getType().isAssignableFrom(Boolean.TYPE)) {
                  var5.setBoolean(this.getModule(), var1);
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public void toggle() {
      this.setValue(!(Boolean)this.getValue());
   }

   public void setValue(Object var1) {
      this.setValue((Boolean)var1);
   }
}
