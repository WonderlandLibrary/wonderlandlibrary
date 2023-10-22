package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanIntegerPropertyBuilder {
   private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanIntegerPropertyBuilder create() {
      return new JavaBeanIntegerPropertyBuilder();
   }

   public JavaBeanIntegerProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Integer.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not an int property");
      } else {
         return new JavaBeanIntegerProperty(var1, this.helper.getBean());
      }
   }

   public JavaBeanIntegerPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanIntegerPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanIntegerPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanIntegerPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanIntegerPropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanIntegerPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanIntegerPropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
