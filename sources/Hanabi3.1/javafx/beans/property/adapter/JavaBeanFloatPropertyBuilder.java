package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanFloatPropertyBuilder {
   private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanFloatPropertyBuilder create() {
      return new JavaBeanFloatPropertyBuilder();
   }

   public JavaBeanFloatProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Float.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not a float property");
      } else {
         return new JavaBeanFloatProperty(var1, this.helper.getBean());
      }
   }

   public JavaBeanFloatPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanFloatPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanFloatPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanFloatPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanFloatPropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanFloatPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanFloatPropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
