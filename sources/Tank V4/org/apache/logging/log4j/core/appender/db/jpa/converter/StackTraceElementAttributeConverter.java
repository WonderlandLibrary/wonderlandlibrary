package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
   autoApply = false
)
public class StackTraceElementAttributeConverter implements AttributeConverter {
   private static final int UNKNOWN_SOURCE = -1;
   private static final int NATIVE_METHOD = -2;

   public String convertToDatabaseColumn(StackTraceElement var1) {
      return var1 == null ? null : var1.toString();
   }

   public StackTraceElement convertToEntityAttribute(String var1) {
      return Strings.isEmpty(var1) ? null : convertString(var1);
   }

   static StackTraceElement convertString(String var0) {
      int var1 = var0.indexOf("(");
      String var2 = var0.substring(0, var1);
      String var3 = var2.substring(0, var2.lastIndexOf("."));
      String var4 = var2.substring(var2.lastIndexOf(".") + 1);
      String var5 = var0.substring(var1 + 1, var0.indexOf(")"));
      String var6 = null;
      int var7 = -1;
      if ("Native Method".equals(var5)) {
         var7 = -2;
      } else if (!"Unknown Source".equals(var5)) {
         int var8 = var5.indexOf(":");
         if (var8 > -1) {
            var6 = var5.substring(0, var8);

            try {
               var7 = Integer.parseInt(var5.substring(var8 + 1));
            } catch (NumberFormatException var10) {
            }
         } else {
            var6 = var5.substring(0);
         }
      }

      return new StackTraceElement(var3, var4, var6, var7);
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((StackTraceElement)var1);
   }
}
