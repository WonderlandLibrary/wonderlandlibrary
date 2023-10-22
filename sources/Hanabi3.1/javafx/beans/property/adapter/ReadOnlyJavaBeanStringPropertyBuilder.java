package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

public final class ReadOnlyJavaBeanStringPropertyBuilder {
   private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

   public static ReadOnlyJavaBeanStringPropertyBuilder create() {
      return new ReadOnlyJavaBeanStringPropertyBuilder();
   }

   public ReadOnlyJavaBeanStringProperty build() throws NoSuchMethodException {
      ReadOnlyPropertyDescriptor var1 = this.helper.getDescriptor();
      if (!String.class.equals(var1.getType())) {
         throw new IllegalArgumentException("Not a String property");
      } else {
         return new ReadOnlyJavaBeanStringProperty(var1, this.helper.getBean());
      }
   }

   public ReadOnlyJavaBeanStringPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public ReadOnlyJavaBeanStringPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public ReadOnlyJavaBeanStringPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public ReadOnlyJavaBeanStringPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public ReadOnlyJavaBeanStringPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }
}
