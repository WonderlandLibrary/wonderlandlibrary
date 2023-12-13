package org.apache.http.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public final class RegistryBuilder {
   private final Map items = new HashMap();

   public static RegistryBuilder create() {
      return new RegistryBuilder();
   }

   RegistryBuilder() {
   }

   public RegistryBuilder register(String var1, Object var2) {
      Args.notEmpty((CharSequence)var1, "ID");
      Args.notNull(var2, "Item");
      this.items.put(var1.toLowerCase(Locale.US), var2);
      return this;
   }

   public Registry build() {
      return new Registry(this.items);
   }

   public String toString() {
      return this.items.toString();
   }
}
