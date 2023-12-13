package com.sun.javafx.fxml.builder;

import java.util.AbstractMap;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.util.Builder;

public class JavaFXImageBuilder extends AbstractMap implements Builder {
   private String url = "";
   private double requestedWidth = 0.0;
   private double requestedHeight = 0.0;
   private boolean preserveRatio = false;
   private boolean smooth = false;
   private boolean backgroundLoading = false;

   public Image build() {
      return new Image(this.url, this.requestedWidth, this.requestedHeight, this.preserveRatio, this.smooth, this.backgroundLoading);
   }

   public Object put(String var1, Object var2) {
      if (var2 != null) {
         String var3 = var2.toString();
         if ("url".equals(var1)) {
            this.url = var3;
         } else if ("requestedWidth".equals(var1)) {
            this.requestedWidth = Double.parseDouble(var3);
         } else if ("requestedHeight".equals(var1)) {
            this.requestedHeight = Double.parseDouble(var3);
         } else if ("preserveRatio".equals(var1)) {
            this.preserveRatio = Boolean.parseBoolean(var3);
         } else if ("smooth".equals(var1)) {
            this.smooth = Boolean.parseBoolean(var3);
         } else {
            if (!"backgroundLoading".equals(var1)) {
               throw new IllegalArgumentException("Unknown Image property: " + var1);
            }

            this.backgroundLoading = Boolean.parseBoolean(var3);
         }
      }

      return null;
   }

   public Set entrySet() {
      throw new UnsupportedOperationException();
   }
}
