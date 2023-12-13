package org.apache.commons.compress.archivers.sevenz;

public class SevenZMethodConfiguration {
   private final SevenZMethod method;
   private final Object options;

   public SevenZMethodConfiguration(SevenZMethod var1) {
      this(var1, (Object)null);
   }

   public SevenZMethodConfiguration(SevenZMethod var1, Object var2) {
      this.method = var1;
      this.options = var2;
      if (var2 != null && !Coders.findByMethod(var1).canAcceptOptions(var2)) {
         throw new IllegalArgumentException("The " + var1 + " method doesn't support options of type " + var2.getClass());
      }
   }

   public SevenZMethod getMethod() {
      return this.method;
   }

   public Object getOptions() {
      return this.options;
   }
}
