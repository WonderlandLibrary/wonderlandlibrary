package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanBooleanPropertyBuilder {
   private final JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanBooleanPropertyBuilder create() {
      return new JavaBeanBooleanPropertyBuilder();
   }

   public JavaBeanBooleanProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Boolean.TYPE.equals(var1.getType()) && !Boolean.class.equals(var1.getType())) {
         throw new IllegalArgumentException("Not a boolean property");
      } else {
         return new JavaBeanBooleanProperty(var1, this.helper.getBean());
      }
   }

   public JavaBeanBooleanPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanBooleanPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanBooleanPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanBooleanPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanBooleanPropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanBooleanPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanBooleanPropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
