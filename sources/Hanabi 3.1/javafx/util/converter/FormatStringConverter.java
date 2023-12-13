package javafx.util.converter;

import java.text.Format;
import java.text.ParsePosition;
import javafx.beans.NamedArg;
import javafx.util.StringConverter;

public class FormatStringConverter extends StringConverter {
   final Format format;

   public FormatStringConverter(@NamedArg("format") Format var1) {
      this.format = var1;
   }

   public Object fromString(String var1) {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.trim();
         if (var1.length() < 1) {
            return null;
         } else {
            Format var2 = this.getFormat();
            ParsePosition var3 = new ParsePosition(0);
            Object var4 = var2.parseObject(var1, var3);
            if (var3.getIndex() != var1.length()) {
               throw new RuntimeException("Parsed string not according to the format");
            } else {
               return var4;
            }
         }
      }
   }

   public String toString(Object var1) {
      if (var1 == null) {
         return "";
      } else {
         Format var2 = this.getFormat();
         return var2.format(var1);
      }
   }

   protected Format getFormat() {
      return this.format;
   }
}
