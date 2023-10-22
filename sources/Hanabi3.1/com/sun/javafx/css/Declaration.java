package com.sun.javafx.css;

import com.sun.javafx.css.converters.URLConverter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;

public final class Declaration {
   final String property;
   final ParsedValueImpl parsedValue;
   final boolean important;
   Rule rule;

   public Declaration(String var1, ParsedValueImpl var2, boolean var3) {
      this.property = var1;
      this.parsedValue = var2;
      this.important = var3;
      if (var1 == null) {
         throw new IllegalArgumentException("propertyName cannot be null");
      } else if (var2 == null) {
         throw new IllegalArgumentException("parsedValue cannot be null");
      }
   }

   public ParsedValue getParsedValue() {
      return this.parsedValue;
   }

   ParsedValueImpl getParsedValueImpl() {
      return this.parsedValue;
   }

   public String getProperty() {
      return this.property;
   }

   public Rule getRule() {
      return this.rule;
   }

   public boolean isImportant() {
      return this.important;
   }

   private StyleOrigin getOrigin() {
      Rule var1 = this.getRule();
      return var1 != null ? var1.getOrigin() : null;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         Declaration var2 = (Declaration)var1;
         if (this.important != var2.important) {
            return false;
         } else if (this.getOrigin() != var2.getOrigin()) {
            return false;
         } else {
            if (this.property == null) {
               if (var2.property == null) {
                  return this.parsedValue == var2.parsedValue || this.parsedValue != null && this.parsedValue.equals(var2.parsedValue);
               }
            } else if (this.property.equals(var2.property)) {
               return this.parsedValue == var2.parsedValue || this.parsedValue != null && this.parsedValue.equals(var2.parsedValue);
            }

            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = 5;
      var1 = 89 * var1 + (this.property != null ? this.property.hashCode() : 0);
      var1 = 89 * var1 + (this.parsedValue != null ? this.parsedValue.hashCode() : 0);
      var1 = 89 * var1 + (this.important ? 1 : 0);
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.property);
      var1.append(": ");
      var1.append(this.parsedValue);
      if (this.important) {
         var1.append(" !important");
      }

      return var1.toString();
   }

   void fixUrl(String var1) {
      if (var1 != null) {
         StyleConverter var2 = this.parsedValue.getConverter();
         ParsedValue[] var3;
         if (var2 == URLConverter.getInstance()) {
            var3 = (ParsedValue[])((ParsedValue[])this.parsedValue.getValue());
            var3[1] = new ParsedValueImpl(var1, (StyleConverter)null);
         } else if (var2 == URLConverter.SequenceConverter.getInstance()) {
            var3 = (ParsedValue[])((ParsedValue[])this.parsedValue.getValue());

            for(int var4 = 0; var4 < var3.length; ++var4) {
               ParsedValue[] var5 = (ParsedValue[])var3[var4].getValue();
               var5[1] = new ParsedValueImpl(var1, (StyleConverter)null);
            }
         }

      }
   }

   final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      var1.writeShort(var2.addString(this.getProperty()));
      this.getParsedValueImpl().writeBinary(var1, var2);
      var1.writeBoolean(this.isImportant());
   }

   static Declaration readBinary(int var0, DataInputStream var1, String[] var2) throws IOException {
      String var3 = var2[var1.readShort()];
      ParsedValueImpl var4 = ParsedValueImpl.readBinary(var0, var1, var2);
      boolean var5 = var1.readBoolean();
      return new Declaration(var3, var4, var5);
   }
}
