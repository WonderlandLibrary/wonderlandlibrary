package com.sun.javafx.fxml.builder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Set;
import javafx.util.Builder;

public class URLBuilder extends AbstractMap implements Builder {
   private ClassLoader classLoader;
   private Object value = null;
   public static final String VALUE_KEY = "value";

   public URLBuilder(ClassLoader var1) {
      this.classLoader = var1;
   }

   public Object put(String var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var1.equals("value")) {
         this.value = var2;
         return null;
      } else {
         throw new IllegalArgumentException(var1 + " is not a valid property.");
      }
   }

   public URL build() {
      if (this.value == null) {
         throw new IllegalStateException();
      } else {
         URL var1;
         if (this.value instanceof URL) {
            var1 = (URL)this.value;
         } else {
            String var2 = this.value.toString();
            if (var2.startsWith("/")) {
               var1 = this.classLoader.getResource(var2);
            } else {
               try {
                  var1 = new URL(var2);
               } catch (MalformedURLException var4) {
                  throw new RuntimeException(var4);
               }
            }
         }

         return var1;
      }
   }

   public Set entrySet() {
      throw new UnsupportedOperationException();
   }
}
