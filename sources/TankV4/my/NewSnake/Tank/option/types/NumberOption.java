package my.NewSnake.Tank.option.types;

import java.lang.reflect.Field;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;

public class NumberOption extends Option {
   public double max;
   private double increment;
   public static boolean asd = true;
   public double min;
   public int increase;

   public double getMax() {
      return this.max;
   }

   public void setValue(Object var1) {
      this.setValue((Number)var1);
   }

   public void setValue(Number var1) {
      super.setValue(var1);
      Field[] var2;
      int var3 = (var2 = this.getModule().getClass().getDeclaredFields()).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         var5.setAccessible(true);
         if (var5.isAnnotationPresent(Option.Op.class) && var5.getName().equalsIgnoreCase(this.getId())) {
            try {
               if (var5.getType().isAssignableFrom(Float.TYPE)) {
                  var5.setFloat(this.getModule(), var1.floatValue());
               } else if (var5.getType().isAssignableFrom(Double.TYPE)) {
                  var5.setDouble(this.getModule(), var1.doubleValue());
               } else if (var5.getType().isAssignableFrom(Long.TYPE)) {
                  var5.setLong(this.getModule(), var1.longValue());
               } else if (var5.getType().isAssignableFrom(Integer.TYPE)) {
                  var5.setLong(this.getModule(), (long)var1.intValue());
               } else if (var5.getType().isAssignableFrom(Short.TYPE)) {
                  var5.setLong(this.getModule(), (long)var1.shortValue());
               } else if (var5.getType().isAssignableFrom(Byte.TYPE)) {
                  var5.setLong(this.getModule(), (long)var1.byteValue());
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
            break;
         }
      }

   }

   public void setIncrement(double var1) {
      this.increment = var1;
   }

   public double getIncrement() {
      return this.increment;
   }

   public void setMin(double var1) {
      this.min = var1;
   }

   public double getMin() {
      return this.min;
   }

   public NumberOption(String var1, String var2, Number var3, Module var4) {
      super(var1, var2, var3, var4);
   }

   public void setMax(double var1) {
      this.max = var1;
   }

   public void deincrement() {
      this.setValue((Number)Math.max(((Number)this.getValue()).doubleValue() - this.increment, this.min));
   }

   public void setValue(String var1) {
      Field[] var2;
      int var3 = (var2 = this.getModule().getClass().getDeclaredFields()).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];
         var5.setAccessible(true);
         if (var5.isAnnotationPresent(Option.Op.class) && var5.getName().equalsIgnoreCase(this.getId())) {
            try {
               if (var5.getType().isAssignableFrom(Float.TYPE)) {
                  super.setValue(Float.parseFloat(var1));
                  var5.setFloat(this.getModule(), Float.parseFloat(var1));
               } else if (var5.getType().isAssignableFrom(Double.TYPE)) {
                  super.setValue(Double.parseDouble(var1));
                  var5.setDouble(this.getModule(), Double.parseDouble(var1));
               } else if (var5.getType().isAssignableFrom(Long.TYPE)) {
                  super.setValue(Math.round(Double.parseDouble(var1)));
                  var5.setLong(this.getModule(), Math.round(Double.parseDouble(var1)));
               } else if (var5.getType().isAssignableFrom(Integer.TYPE)) {
                  super.setValue((int)Math.round(Double.parseDouble(var1)));
                  var5.setInt(this.getModule(), (int)Math.round(Double.parseDouble(var1)));
               } else if (var5.getType().isAssignableFrom(Short.TYPE)) {
                  super.setValue((short)((int)Math.round(Double.parseDouble(var1))));
                  var5.setShort(this.getModule(), (short)((int)Math.round(Double.parseDouble(var1))));
               } else if (var5.getType().isAssignableFrom(Byte.TYPE)) {
                  super.setValue((byte)((int)Math.round(Double.parseDouble(var1))));
                  var5.setByte(this.getModule(), (byte)((int)Math.round(Double.parseDouble(var1))));
               }
            } catch (Exception var7) {
               var7.printStackTrace();
            }
            break;
         }
      }

   }

   public void increment() {
      this.setValue((Number)Math.min(((Number)this.getValue()).doubleValue() + this.increment, this.max));
   }
}
