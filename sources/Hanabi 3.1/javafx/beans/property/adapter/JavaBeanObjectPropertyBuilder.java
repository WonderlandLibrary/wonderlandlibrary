package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanObjectPropertyBuilder {
   private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanObjectPropertyBuilder create() {
      return new JavaBeanObjectPropertyBuilder();
   }

   public JavaBeanObjectProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      return new JavaBeanObjectProperty(var1, this.helper.getBean());
   }

   public JavaBeanObjectPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanObjectPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanObjectPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanObjectPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanObjectPropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanObjectPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanObjectPropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
