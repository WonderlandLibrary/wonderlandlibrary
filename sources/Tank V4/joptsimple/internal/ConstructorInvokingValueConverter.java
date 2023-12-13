package joptsimple.internal;

import java.lang.reflect.Constructor;
import joptsimple.ValueConverter;

class ConstructorInvokingValueConverter implements ValueConverter {
   private final Constructor ctor;

   ConstructorInvokingValueConverter(Constructor var1) {
      this.ctor = var1;
   }

   public Object convert(String var1) {
      return Reflection.instantiate(this.ctor, var1);
   }

   public Class valueType() {
      return this.ctor.getDeclaringClass();
   }

   public String valuePattern() {
      return null;
   }
}
