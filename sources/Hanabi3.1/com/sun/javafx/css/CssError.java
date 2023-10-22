package com.sun.javafx.css;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.Scene;

public class CssError {
   private static Reference SCENE_REF;
   private final Reference sceneRef;
   protected final String message;

   public static void setCurrentScene(Scene var0) {
      if (StyleManager.getErrors() != null) {
         if (var0 != null) {
            Scene var1 = SCENE_REF != null ? (Scene)SCENE_REF.get() : null;
            if (var1 != var0) {
               SCENE_REF = new WeakReference(var0);
            }
         } else {
            SCENE_REF = null;
         }

      }
   }

   public final String getMessage() {
      return this.message;
   }

   public CssError(String var1) {
      this.message = var1;
      this.sceneRef = SCENE_REF;
   }

   public Scene getScene() {
      return this.sceneRef != null ? (Scene)this.sceneRef.get() : null;
   }

   public String toString() {
      return "CSS Error: " + this.message;
   }

   public static final class PropertySetError extends CssError {
      private final CssMetaData styleableProperty;
      private final Styleable styleable;

      public PropertySetError(CssMetaData var1, Styleable var2, String var3) {
         super(var3);
         this.styleableProperty = var1;
         this.styleable = var2;
      }

      public Styleable getStyleable() {
         return this.styleable;
      }

      public CssMetaData getProperty() {
         return this.styleableProperty;
      }
   }

   public static final class StringParsingError extends CssError {
      private final String style;

      public StringParsingError(String var1, String var2) {
         super(var2);
         this.style = var1;
      }

      public String getStyle() {
         return this.style;
      }

      public String toString() {
         return "CSS Error parsing '" + this.style + ": " + this.message;
      }
   }

   public static final class InlineStyleParsingError extends CssError {
      private final Styleable styleable;

      public InlineStyleParsingError(Styleable var1, String var2) {
         super(var2);
         this.styleable = var1;
      }

      public Styleable getStyleable() {
         return this.styleable;
      }

      public String toString() {
         String var1 = this.styleable.getStyle();
         String var2 = this.styleable.toString();
         return "CSS Error parsing in-line style '" + var1 + "' from " + var2 + ": " + this.message;
      }
   }

   public static final class StylesheetParsingError extends CssError {
      private final String url;

      public StylesheetParsingError(String var1, String var2) {
         super(var2);
         this.url = var1;
      }

      public String getURL() {
         return this.url;
      }

      public String toString() {
         String var1 = this.url != null ? this.url : "?";
         return "CSS Error parsing " + var1 + ": " + this.message;
      }
   }
}
