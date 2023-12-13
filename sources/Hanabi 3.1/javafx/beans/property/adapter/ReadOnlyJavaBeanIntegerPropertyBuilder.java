package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

public final class ReadOnlyJavaBeanIntegerPropertyBuilder {
   private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

   public static ReadOnlyJavaBeanIntegerPropertyBuilder create() {
      return new ReadOnlyJavaBeanIntegerPropertyBuilder();
   }

   public ReadOnlyJavaBeanIntegerProperty build() throws NoSuchMethodException {
      ReadOnlyPropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Integer.TYPE.equals(var1.getType()) && !Number.class.isAssignableFrom(var1.getType())) {
         throw new IllegalArgumentException("Not an int property");
      } else {
         return new ReadOnlyJavaBeanIntegerProperty(var1, this.helper.getBean());
      }
   }

   public ReadOnlyJavaBeanIntegerPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public ReadOnlyJavaBeanIntegerPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public ReadOnlyJavaBeanIntegerPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public ReadOnlyJavaBeanIntegerPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public ReadOnlyJavaBeanIntegerPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }
}
