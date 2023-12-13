package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanDoublePropertyBuilder {
   private final JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanDoublePropertyBuilder create() {
      return new JavaBeanDoublePropertyBuilder();
   }

   public JavaBeanDoubleProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Double.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not a double property");
      } else {
         return new JavaBeanDoubleProperty(var1, this.helper.getBean());
      }
   }

   public JavaBeanDoublePropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanDoublePropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanDoublePropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanDoublePropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanDoublePropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanDoublePropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanDoublePropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
