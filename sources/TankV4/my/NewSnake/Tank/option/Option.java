package my.NewSnake.Tank.option;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import my.NewSnake.Tank.module.Module;

public class Option {
   private String name;
   public Object value;
   private Module module;
   private List optionList = new ArrayList();
   private String id;

   public List getOptions() {
      return this.optionList;
   }

   public String getDisplayName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   public Module getModule() {
      return this.module;
   }

   public Option(String var1, String var2, Object var3, Module var4) {
      this.id = var1;
      this.name = var2;
      this.value = var3;
      this.module = var4;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }

   public String getId() {
      return this.id;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   public @interface Op {
      double max() default 10.0D;

      double increment() default 1.0D;

      double min() default 1.0D;

      String name() default "null";
   }
}
