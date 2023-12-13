package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanLongPropertyBuilder {
   private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanLongPropertyBuilder create() {
      return new JavaBeanLongPropertyBuilder();
   }

   public JavaBeanLongProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Long.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not a long property");
      } else {
         return new JavaBeanLongProperty(var1, this.helper.getBean());
      }
   }

   public JavaBeanLongPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanLongPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanLongPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanLongPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanLongPropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanLongPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanLongPropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
