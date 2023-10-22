package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

public final class ReadOnlyJavaBeanBooleanPropertyBuilder {
   private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

   public static ReadOnlyJavaBeanBooleanPropertyBuilder create() {
      return new ReadOnlyJavaBeanBooleanPropertyBuilder();
   }

   public ReadOnlyJavaBeanBooleanProperty build() throws NoSuchMethodException {
      ReadOnlyPropertyDescriptor var1 = this.helper.getDescriptor();
      if (!Boolean.TYPE.equals(var1.getType()) && !Boolean.class.equals(var1.getType())) {
         throw new IllegalArgumentException("Not a boolean property");
      } else {
         return new ReadOnlyJavaBeanBooleanProperty(var1, this.helper.getBean());
      }
   }

   public ReadOnlyJavaBeanBooleanPropertyBuilder name(String var1) {
      this.helper.name(var1);
      return this;
   }

   public ReadOnlyJavaBeanBooleanPropertyBuilder bean(Object var1) {
      this.helper.bean(var1);
      return this;
   }

   public ReadOnlyJavaBeanBooleanPropertyBuilder beanClass(Class var1) {
      this.helper.beanClass(var1);
      return this;
   }

   public ReadOnlyJavaBeanBooleanPropertyBuilder getter(String var1) {
      this.helper.getterName(var1);
      return this;
   }

   public ReadOnlyJavaBeanBooleanPropertyBuilder getter(Method var1) {
      this.helper.getter(var1);
      return this;
   }
}
