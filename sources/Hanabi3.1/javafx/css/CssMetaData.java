package javafx.css;

import java.util.Collections;
import java.util.List;

public abstract class CssMetaData {
   private final String property;
   private final StyleConverter converter;
   private final Object initialValue;
   private final List subProperties;
   private final boolean inherits;

   /** @deprecated */
   @Deprecated
   public void set(Styleable var1, Object var2, StyleOrigin var3) {
      StyleableProperty var4 = this.getStyleableProperty(var1);
      StyleOrigin var5 = var4.getStyleOrigin();
      Object var6 = var4.getValue();
      if (var5 == var3) {
         if (var6 != null) {
            if (var6.equals(var2)) {
               return;
            }
         } else if (var2 == null) {
            return;
         }
      }

      var4.applyStyle(var3, var2);
   }

   public abstract boolean isSettable(Styleable var1);

   public abstract StyleableProperty getStyleableProperty(Styleable var1);

   public final String getProperty() {
      return this.property;
   }

   public final StyleConverter getConverter() {
      return this.converter;
   }

   public Object getInitialValue(Styleable var1) {
      return this.initialValue;
   }

   public final List getSubProperties() {
      return this.subProperties;
   }

   public final boolean isInherits() {
      return this.inherits;
   }

   protected CssMetaData(String var1, StyleConverter var2, Object var3, boolean var4, List var5) {
      this.property = var1;
      this.converter = var2;
      this.initialValue = var3;
      this.inherits = var4;
      this.subProperties = var5 != null ? Collections.unmodifiableList(var5) : null;
      if (this.property == null || this.converter == null) {
         throw new IllegalArgumentException("neither property nor converter can be null");
      }
   }

   protected CssMetaData(String var1, StyleConverter var2, Object var3, boolean var4) {
      this(var1, var2, var3, var4, (List)null);
   }

   protected CssMetaData(String var1, StyleConverter var2, Object var3) {
      this(var1, var2, var3, false, (List)null);
   }

   protected CssMetaData(String var1, StyleConverter var2) {
      this(var1, var2, (Object)null, false, (List)null);
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         CssMetaData var2 = (CssMetaData)var1;
         if (this.property == null) {
            if (var2.property != null) {
               return false;
            }
         } else if (!this.property.equals(var2.property)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = 3;
      var1 = 19 * var1 + (this.property != null ? this.property.hashCode() : 0);
      return var1;
   }

   public String toString() {
      return "CSSProperty {" + "property: " + this.property + ", converter: " + this.converter.toString() + ", initalValue: " + this.initialValue + ", inherits: " + this.inherits + ", subProperties: " + (this.subProperties != null ? this.subProperties.toString() : "[]") + "}";
   }
}
