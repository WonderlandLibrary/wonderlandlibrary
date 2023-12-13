package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

public final class ReadOnlyJavaBeanDoublePropertyBuilder {
   private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

   public static ReadOnlyJavaBeanDoublePropertyBuilder create() {
      return new ReadOnlyJavaBeanDoublePropertyBuilder();
   }

   public ReadOnlyJavaBeanDoubleProperty build() throws NoSuchMethodException {
      ReadOnlyPropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Double.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not a double property");
      } else {
         return new ReadOnlyJavaBeanDoubleProperty(var1, this.helper.getBean());
      }
   }

   public ReadOnlyJavaBeanDoublePropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public ReadOnlyJavaBeanDoublePropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public ReadOnlyJavaBeanDoublePropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public ReadOnlyJavaBeanDoublePropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public ReadOnlyJavaBeanDoublePropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }
}
