package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.helpers.Strings;

@Converter(
   autoApply = false
)
public class MarkerAttributeConverter implements AttributeConverter {
   public String convertToDatabaseColumn(Marker var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var2 = new StringBuilder(var1.getName());
         Marker var3 = var1.getParent();
         int var4 = 0;

         boolean var5;
         for(var5 = false; var3 != null; var3 = var3.getParent()) {
            ++var4;
            var5 = true;
            var2.append("[ ").append(var3.getName());
         }

         for(int var6 = 0; var6 < var4; ++var6) {
            var2.append(" ]");
         }

         if (var5) {
            var2.append(" ]");
         }

         return var2.toString();
      }
   }

   public Marker convertToEntityAttribute(String var1) {
      if (Strings.isEmpty(var1)) {
         return null;
      } else {
         int var2 = var1.indexOf("[");
         return var2 < 1 ? MarkerManager.getMarker(var1) : MarkerManager.getMarker(var1.substring(0, var2));
      }
   }

   public Object convertToEntityAttribute(Object var1) {
      return this.convertToEntityAttribute((String)var1);
   }

   public Object convertToDatabaseColumn(Object var1) {
      return this.convertToDatabaseColumn((Marker)var1);
   }
}
