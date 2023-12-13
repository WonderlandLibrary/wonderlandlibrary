package joptsimple.internal;

import java.lang.reflect.Method;
import joptsimple.ValueConverter;

class MethodInvokingValueConverter implements ValueConverter {
   private final Method method;
   private final Class clazz;

   MethodInvokingValueConverter(Method var1, Class var2) {
      this.method = var1;
      this.clazz = var2;
   }

   public Object convert(String var1) {
      return this.clazz.cast(Reflection.invoke(this.method, var1));
   }

   public Class valueType() {
      return this.clazz;
   }

   public String valuePattern() {
      return null;
   }
}
