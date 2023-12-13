package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

public final class JavaBeanStringPropertyBuilder {
   private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

   public static JavaBeanStringPropertyBuilder create() {
      return new JavaBeanStringPropertyBuilder();
   }

   public JavaBeanStringProperty build() throws NoSuchMethodException {
      PropertyDescriptor var1 = this.helper.getDescriptor();
      if (!String.class.equals(var1.getType())) {
         throw new IllegalArgumentException("Not a String property");
      } else {
         return new JavaBeanStringProperty(var1, this.helper.getBean());
      }
   }

   public JavaBeanStringPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public JavaBeanStringPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public JavaBeanStringPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public JavaBeanStringPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public JavaBeanStringPropertyBuilder setter(String var1) {
      this.helper.setterName(var1);
      return this;
   }

   public JavaBeanStringPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }

   public JavaBeanStringPropertyBuilder setter(Method var1) {
      this.helper.setter(var1);
      return this;
   }
}
