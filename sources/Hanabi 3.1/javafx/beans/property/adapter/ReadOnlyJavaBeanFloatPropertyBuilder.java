package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

public final class ReadOnlyJavaBeanFloatPropertyBuilder {
   private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

   public static ReadOnlyJavaBeanFloatPropertyBuilder create() {
      return new ReadOnlyJavaBeanFloatPropertyBuilder();
   }

   public ReadOnlyJavaBeanFloatProperty build() throws NoSuchMethodException {
      ReadOnlyPropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Float.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not a float property");
      } else {
         return new ReadOnlyJavaBeanFloatProperty(var1, this.helper.getBean());
      }
   }

   public ReadOnlyJavaBeanFloatPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public ReadOnlyJavaBeanFloatPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public ReadOnlyJavaBeanFloatPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public ReadOnlyJavaBeanFloatPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public ReadOnlyJavaBeanFloatPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }
}
