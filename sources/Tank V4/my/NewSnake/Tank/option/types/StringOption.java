package my.NewSnake.Tank.option.types;

import java.lang.reflect.Field;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

public class StringOption extends Option {
   public StringOption(String var1, String var2, String var3, Module var4) {
      super(var1, var2, var3, var4);
   }

   public void setValue(String var1) {
      super.setValue(var1);
      Field[] var2;
      int var3 = (var2 = this.getModule().getClass().getDeclaredFields()).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         var5.setAccessible(true);
         if (var5.isAnnotationPresent(Option.Op.class) && var5.getName().equalsIgnoreCase(this.getId())) {
            try {
               if (var5.getType().isAssignableFrom(String.class)) {
                  var5.set(this.getModule(), var1);
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public void setValue(Object var1) {
      this.setValue((String)var1);
   }
}
